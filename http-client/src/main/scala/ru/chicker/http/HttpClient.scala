package ru.chicker.http

import io.circe.Decoder
import java.net.URI
import cats._
import cats.implicits._
import cats.effect.{Resource, Blocker}
import sttp.client3.SttpBackend
import sttp.client3.asynchttpclient.fs2.AsyncHttpClientFs2Backend
import cats.effect.ConcurrentEffect
import cats.effect.ContextShift

trait HttpClient[F[_]] {
  def strictGet[T: Decoder](uri: URI): F[T]
}

object HttpClient {

  def apply[F[_]: Monad: ConcurrentEffect: ContextShift](blocker: Blocker): Resource[F, HttpClient[F]] = {
    for {
      backend <- Resource.make(AsyncHttpClientFs2Backend(blocker))(_.close())
    } yield new SttpClient[F](backend)
  }

  private final class SttpClient[F[_]](backend: SttpBackend[F, Any])(implicit M: MonadError[F, Throwable]) extends HttpClient[F] {
    import sttp.client3._
    import sttp.client3.circe._
    import sttp.model._

    def strictGet[T: Decoder](uri: URI): F[T] = {
      val request =
        basicRequest.get(Uri(uri)).response(asJson[T])
      val body = for {
        resp <- request.send(backend)
      } yield resp.body

      body.flatMap(_.fold(e => M.raiseError(e), _.pure[F]))
    }
  }
}