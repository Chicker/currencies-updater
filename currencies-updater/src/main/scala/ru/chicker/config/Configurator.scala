package ru.chicker.config

import cats.effect.Sync

trait Configurator[F[_]] {
  def loadConfig(): F[Config]
}

object Configurator {
  def apply[F[_]](implicit instance: Configurator[F]): Configurator[F] = instance
  def purecfg[F[_]: Sync]: Configurator[F] = new Impl[F]

  private final class Impl[F[_]: Sync] extends Configurator[F] {

    def loadConfig(): F[Config] = {
      import pureconfig.generic.auto._

      Sync[F].delay {
        pureconfig.ConfigSource.default.loadOrThrow[Config]
      }
    }
  }
}
