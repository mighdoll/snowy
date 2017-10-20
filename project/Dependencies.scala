import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Dependencies {
  object V {
    val scala    = "2.12.4"
    val akka     = "2.5.6"
    val akkaHttp = "10.0.10"
    val jackson  = "2.9.2"
    val log4j    = "2.9.1"
  }

  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.13.5"
  val scalactic  = "org.scalactic"  %% "scalactic"  % "3.0.4"
  val scalaTest  = "org.scalatest"  %% "scalatest"  % "3.0.4"

  val loggingProvider = Seq(
    "org.apache.logging.log4j"         % "log4j-core"              % V.log4j,
    "org.apache.logging.log4j"         % "log4j-slf4j-impl"        % V.log4j,
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % V.jackson,
    "com.fasterxml.jackson.core"       % "jackson-databind"        % V.jackson
  )

  val scalaLogging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
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
    "org.typelevel" %% "cats"           % "0.9.0",
    "org.typelevel" %% "alleycats-core" % "0.2.0"
  )

  val scalaJSDomSetting = Def.setting(
    Seq("org.scala-js" %%% "scalajs-dom" % "0.9.3")
  )

  val booPickleSetting = Def.setting(
    Seq("io.suzaku" %%% "boopickle" % "1.2.6")
  )

  val jsLibraries = Seq(
    "org.webjars.bower" % "three.js"                   % "0.87.0" / "0.87.0/three.min.js",
    "org.webjars.bower" % "github-com-mrdoob-stats-js" % "r17" / "r17/build/stats.min.js"
  )

  val akkaLoad = Seq(
    "com.typesafe.akka"   %% "akka-testkit"        % V.akka,
    "com.typesafe.akka"   %% "akka-stream-testkit" % V.akka,
    "org.asynchttpclient" % "async-http-client"    % "2.1.0-alpha25"
  )

  val orientdb = Seq(
    "com.orientechnologies" % "orientdb-graphdb" % "2.2.29"
  )

  val macroParadise = "org.scalamacros" % "paradise" % "2.1.1"
}
