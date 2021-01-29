package ru.chicker.config

final case class Config(db: Config.Db)

object Config {
  case class Db(jdbcDriver: String, jdbcUri: String, user: String, pass: String)
}
