import sbt._

object Dependencies {
  object Versions {
    lazy val tofu = "0.9.0"
    lazy val scalaTest = "3.2.2"
  }

  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest
  lazy val tofuCore = "ru.tinkoff" %% "tofu-core" % Versions.tofu
}
