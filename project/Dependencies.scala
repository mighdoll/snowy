import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Dependencies {
  object V {
    val scala    = "2.12.4"
    val akka     = "2.5.11"
    val akkaHttp = "10.1.1"
    val jackson  = "2.9.5"
    val log4j    = "2.11.0"
  }

  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.13.5"
  val scalactic  = "org.scalactic"  %% "scalactic"  % "3.0.5"
  val scalaTest  = "org.scalatest"  %% "scalatest"  % "3.0.5"

  val loggingProvider = Seq(
    "org.apache.logging.log4j"         % "log4j-core"              % V.log4j,
    "org.apache.logging.log4j"         % "log4j-slf4j-impl"        % V.log4j,
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % V.jackson,
    "com.fasterxml.jackson.core"       % "jackson-databind"        % V.jackson
  )

  val scalaLogging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"
  )

  val akkaStreams = Seq(
    "com.typesafe.akka" %% "akka-actor"  % V.akka,
    "com.typesafe.akka" %% "akka-stream" % V.akka
  )

  val scopt = Seq(
    "com.github.scopt" %% "scopt" % "3.7.0"
  )

  val squants = Seq(
    "org.typelevel" %% "squants" % "1.3.0"
  )

  val log4j = Seq(
    "org.apache.logging.log4j" % "log4j-jul" % V.log4j
  )

  val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http" % V.akkaHttp
  )

  val akka = Seq(
    "com.typesafe.akka" %% "akka-slf4j" % V.akka
  )

  val cats = Seq(
    "org.typelevel" %% "cats-core" % "1.1.0"
  )

  val scalaJSDomSetting = Def.setting(
    Seq("org.scala-js" %%% "scalajs-dom" % "0.9.5")
  )

  val booPickleSetting = Def.setting(
    Seq("io.suzaku" %%% "boopickle" % "1.3.0")
  )

  val jsLibraries = Seq(
    "org.webjars.npm" % "three"    % "0.91.0" / "0.91.0/build/three.min.js",
    "org.webjars.npm" % "stats.js" % "0.17.0" / "0.17.0/build/stats.min.js"
  )

  val akkaLoad = Seq(
    "com.typesafe.akka"   %% "akka-testkit"        % V.akka,
    "com.typesafe.akka"   %% "akka-stream-testkit" % V.akka,
    "org.asynchttpclient" % "async-http-client"    % "2.4.4"
  )

  val orientdb = Seq(
    "com.orientechnologies" % "orientdb-graphdb" % "2.2.33"
  )

  val macroParadise = "org.scalamacros" % "paradise" % "2.1.1"
}
