package ru.chicker.db

import cats.effect.Sync
import org.flywaydb.core.Flyway

import ru.chicker.config.Config

trait Migrator[F[_]] {
  def migrate: F[Unit]
  def reset(): F[Unit]
}

object Migrator {
  def apply[F[_]](implicit instance: Migrator[F]): Migrator[F] = instance

  def flyway[F[_]: Sync](conf: Config.Db): F[Migrator[F]] = Sync[F].delay(new FlywayDb[F](conf))

  private class FlywayDb[F[_]: Sync](conf: Config.Db) extends Migrator[F] {

    private val flywayConfiguration = {
      Class.forName(conf.jdbcDriver)
      Flyway.configure().dataSource(conf.jdbcUri, conf.user, conf.pass).locations("classpath:db/migration")
    }

    private val flyway = new Flyway(flywayConfiguration)

    override def migrate: F[Unit] = {
      Sync[F].delay {
        flyway.migrate().migrationsExecuted
      }
    }

    override def reset(): F[Unit] = {
      Sync[F].delay {
        flyway.clean()
        flyway.migrate().migrationsExecuted
      }
    }
  }
}
