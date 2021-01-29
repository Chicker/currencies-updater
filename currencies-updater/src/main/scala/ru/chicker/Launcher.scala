package ru.chicker

import cats.Applicative
import cats.Monad
import cats.effect.Blocker
import cats.effect.ConcurrentEffect
import cats.effect.ContextShift
import cats.effect.Resource
import cats.implicits._
import monix.eval.Task
import monix.execution.Scheduler
import ru.chicker.config._
import ru.chicker.db._
import ru.chicker.http.HttpClient
import ru.chicker.tofuext.RaceExt
import tofu.common.Console
import tofu.logging._
import tofu.syntax.logging._

object Launcher extends App {
  // import monix.eval.instances._
  import ru.chicker.tofuext.instances.monixtask._

  def app[F[_]: Monad: Applicative: ConcurrentEffect: ContextShift: Console: RaceExt] /*(implicit
      r: Raise[F, AppError]
  )*/: F[Unit] = {
    val resource = for {
      implicit0(logging: Logging[F]) <- Resource.liftF[F, Logging[F]](Logs.sync[F, F].byName("app"))
      implicit0(configurator: Configurator[F]) = Configurator.purecfg[F]
      config                           <- Resource.liftF[F, Config](Configurator[F].loadConfig())
      implicit0(migrator: Migrator[F]) <- Resource.liftF[F, Migrator[F]](Migrator.flyway(config.db))
      blocker                          <- Blocker[F]
      _                                <- Resource.liftF[F, Unit](Migrator[F].migrate)
      xa                               <- db.mkTransactor(config.db, blocker)
      httpClient                       <- HttpClient[F](blocker)
    } yield (logging, httpClient, xa, config)

    resource.use { case (logging, httpClient, xa, config) =>
      implicit val log = logging
      info"Starting program..." *> Program[F](httpClient, xa, config).recoverWith {
        case e => errorCause"Unexpected error: ${e.getMessage}"(e)
      }
    }
  }

  implicit private val scheduler = Scheduler(scala.concurrent.ExecutionContext.global)

  app[Task].runSyncUnsafe()
}
