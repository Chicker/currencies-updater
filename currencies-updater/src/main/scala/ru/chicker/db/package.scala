package ru.chicker

import cats.effect._

import ru.chicker.config.Config

package object db {
  import doobie._
  // import doobie.implicits._
  import doobie.hikari._

  def mkTransactor[F[_]: Async: ContextShift](config: Config.Db, blocker: Blocker): Resource[F, HikariTransactor[F]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](8)
      xa <- HikariTransactor.newHikariTransactor[F](
        config.jdbcDriver,
        config.jdbcUri, // jdbc URI
        config.user, // username
        config.pass, // password
        ce,
        blocker
      )
    } yield xa
}
