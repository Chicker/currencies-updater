import sbt._

object Dependencies {
  object Versions {
    lazy val tofu = "0.9.0"
    lazy val scalaTest = "3.2.2"
    lazy val sttp = "3.0.0"
    lazy val circe = "0.13.0"
    lazy val doobie = "0.9.0"
    lazy val monix = "3.3.0"
  }

  // Compiler plugins
  lazy val bm4 = "com.olegpy" %% "better-monadic-for" % "0.3.1"
  
  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest
  lazy val tofuCore = "ru.tinkoff" %% "tofu-core" % Versions.tofu
  
  lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val tofuLogging = "ru.tinkoff" %% "tofu-logging" % Versions.tofu
  
  lazy val sttpCore = "com.softwaremill.sttp.client3" %% "core" % Versions.sttp
  lazy val sttpCirce = "com.softwaremill.sttp.client3" %% "circe" % Versions.sttp
  lazy val sttpAsyncHttpFs2Backend = "com.softwaremill.sttp.client3" %% "async-http-client-backend-fs2" % Versions.sttp
  lazy val circeCore = "io.circe" %% "circe-core" % Versions.circe
  lazy val circeGeneric = "io.circe" %% "circe-generic" % Versions.circe
  lazy val circeParser = "io.circe" %% "circe-parser" % Versions.circe

  lazy val doobieCore = "org.tpolecat" %% "doobie-core" % Versions.doobie
  lazy val doobiePostgres = "org.tpolecat" %% "doobie-postgres"  % Versions.doobie
  lazy val doobieHikari = "org.tpolecat" %% "doobie-hikari"  % Versions.doobie

  lazy val flyway = "org.flywaydb" % "flyway-core" % "7.5.1"
  lazy val pureconfig = "com.github.pureconfig" %% "pureconfig" % "0.14.0"

  lazy val monixEval = "io.monix" %% "monix-eval" % Versions.monix
}
