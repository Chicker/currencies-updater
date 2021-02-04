import Dependencies._

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val mainCompilerOptions = Seq(
  "-deprecation", // warn about use of deprecated APIs
  "-feature", // warn about misused language features
  "-unchecked", // warn about unchecked type parameters
  "-Xlint:_,-byname-implicit", // enable handy linter warnings without byname implicit
  "-Werror", // "-Xfatal-warnings",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused:imports",
  "-Ymacro-annotations",
  "-language:implicitConversions",
  "-language:higherKinds" // allow higher kinded types without `import scala.language.higherKinds`
)

val commonSettings = Seq(
  scalacOptions ++= mainCompilerOptions,
  libraryDependencies ++= Seq(
    monixEval
  ),
  addCompilerPlugin(bm4)
)

lazy val `currencies-updater` = (project in file("currencies-updater"))
  .settings(commonSettings: _*)
  .settings(
    name := "currencies-updater",
    libraryDependencies ++= Seq(
      logbackClassic,
      tofuCore,
      tofuLogging,
      pureconfig,
      circeCore,
      circeGeneric,
      doobieCore,
      doobiePostgres,
      doobieHikari,
      flyway,
      scalaTest % Test
    )
  )
  .enablePlugins(JavaAppPackaging)
  .dependsOn(`http-client`)
  .dependsOn(`tofu-ext`)

lazy val `http-client` = (project in file("http-client"))
  .settings(commonSettings: _*)
  .settings(
    name := "http-client",
    libraryDependencies ++= Seq(
      logbackClassic,
      tofuCore,
      tofuLogging,
      pureconfig,
      sttpCore,
      sttpCirce,
      sttpAsyncHttpFs2Backend,
      circeCore,
      circeGeneric,
      scalaTest % Test
    )
  )

lazy val `tofu-ext` = (project in file("tofu-ext"))
  .settings(commonSettings: _*)
  .settings(
    name := "tofu-ext",
    libraryDependencies ++= Seq(
      tofuCore,
      scalaTest % Test
    )
  )