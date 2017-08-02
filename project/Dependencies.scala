import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Dependencies {
  object V {
    val scala      = "2.12.3"
    val akka       = "2.5.3"
    val akkaHttp   = "10.0.9"
    val jackson    = "2.9.0"
    val log4j      = "2.8.2"
    val scalacheck = "1.13.5"
    val scalactic  = "3.0.3"
    val scalatest  = "3.0.3"
  }

  val scalaCheck = "org.scalacheck" %% "scalacheck" % V.scalacheck
  val scalactic = "org.scalactic"  %% "scalactic"  % V.scalactic
  val scalaTest = "org.scalatest"  %% "scalatest"  % V.scalatest

  val scalaJSCheck = "org.scalacheck" %%%! "scalacheck" % V.scalacheck
  val scalaJSctic = "org.scalactic"  %%%! "scalactic"  % V.scalactic
  val scalaJSTest = "org.scalatest"  %%%! "scalatest"  % V.scalatest

  val loggingProvider = Seq(
    "org.apache.logging.log4j"         % "log4j-core"              % V.log4j,
    "org.apache.logging.log4j"         % "log4j-slf4j-impl"        % V.log4j,
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % V.jackson,
    "com.fasterxml.jackson.core"       % "jackson-databind"        % V.jackson
  )

  lazy val scalaLogging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
  )

  lazy val akkaStreams = Seq(
    "com.typesafe.akka" %% "akka-actor"  % V.akka,
    "com.typesafe.akka" %% "akka-stream" % V.akka
  )

  lazy val scopt = Seq(
    "com.github.scopt" %% "scopt" % "3.6.0"
  )

  lazy val squants = Seq(
    "org.typelevel"            %% "squants"    % "1.3.0"
  )

  lazy val log4j = Seq(
    "org.apache.logging.log4j" % "log4j-jul"   % V.log4j
  )

  lazy val akkaHttp = Seq(
    "com.typesafe.akka"        %% "akka-http"  % V.akkaHttp
  )

  lazy val akka = Seq(
    "com.typesafe.akka"        %% "akka-slf4j" % V.akka
  )

  lazy val cats = Seq(
    "org.typelevel"            %% "cats"       % "0.9.0"
  )

  lazy val scalaJSDom = Seq(
    "org.scala-js" %%%! "scalajs-dom" % "0.9.3"
  )

  lazy val jsLibraries = Seq(
    "org.webjars.bower" % "three.js"                   % "0.86.0" / "0.86.0/three.min.js",
    "org.webjars.bower" % "github-com-mrdoob-stats-js" % "r17" / "r17/build/stats.min.js"
  )

  lazy val akkaLoad = Seq(
    "com.typesafe.akka"   %% "akka-testkit"        % V.akka,
    "com.typesafe.akka"   %% "akka-stream-testkit" % V.akka,
    "org.asynchttpclient" % "async-http-client"    % "2.1.0-alpha22"
  )

  lazy val boopickle = "io.suzaku" %%%! "boopickle" % "1.2.6"

  lazy val orientdb = Seq(
    "com.orientechnologies" % "orientdb-graphdb" % "2.2.23"
  )

  lazy val macroParadise = "org.scalamacros" % "paradise" % "2.1.1"
}
