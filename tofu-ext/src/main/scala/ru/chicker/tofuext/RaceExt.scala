package ru.chicker.tofuext

import cats.effect.Concurrent
import cats.effect.concurrent.Deferred
import cats.effect.concurrent.Semaphore
import cats.implicits._

trait RaceExt[F[_]] {
  def raceMany[A](fas: Iterable[F[A]]): F[A]

  /// Ignore all failed tasks while there other running tasks are
  /// Returns the first winner and cancelles other.
  /// If all partipicants are failed, fails `F[A]` with the first error
  def raceManyIgnoreErrors[A](fas: Iterable[F[A]])(implicit F: Concurrent[F]): F[A] = {
    def safeSetError(firstError: Deferred[F, Throwable], t: Throwable): F[Unit] =
      F.handleError(firstError.complete(t))(_ => ())
    for {
      firstError <- Deferred[F, Throwable]
      sem        <- Semaphore[F](0)
      raceRes <- Concurrent[F].race(
        sem.acquireN(fas.size.toLong),
        raceMany(
          fas.map(fa => fa.handleErrorWith(t => safeSetError(firstError, t) *> sem.release *> Concurrent[F].never))
        )
      )
      // if the left task was completed first, it means all tasks are failed. Fail `F[A]` with the first error.
      res <- raceRes.fold(_ => firstError.get flatMap Concurrent[F].raiseError[A], _.pure[F])
    } yield res
  }
}

object RaceExt {
  def apply[F[_]](implicit instance: RaceExt[F]): RaceExt[F] = instance
}
