import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Dependencies {
  object V {
    val scala    = "2.12.6"
    val akka     = "2.5.12"
    val akkaHttp = "10.1.1"
  }

  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.0"
  val scalactic  = "org.scalactic"  %% "scalactic"  % "3.0.5"
  val scalaTest  = "org.scalatest"  %% "scalatest"  % "3.0.5"

  val scalaLogging = Seq(
    "com.outr" %% "scribe" % "2.4.0"
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

  val akka = Seq(
    "com.typesafe.akka" %% "akka-http"  % V.akkaHttp
  )

  val cats = Seq(
    "org.typelevel" %% "cats-core" % "1.1.0"
  )

  val scalaJSDomSetting = Def.setting(
    Seq("org.scala-js" %%% "scalajs-dom" % "0.9.6")
  )

  val booPickleSetting = Def.setting(
    Seq("io.suzaku" %%% "boopickle" % "1.3.0")
  )

  val jsLibraries = Seq(
    "org.webjars.npm" % "three"    % "0.92.0" / "0.92.0/build/three.min.js",
    "org.webjars.npm" % "stats.js" % "0.17.0" / "0.17.0/build/stats.min.js"
  )

  val akkaLoad = Seq(
    "com.typesafe.akka"   %% "akka-testkit"        % V.akka,
    "com.typesafe.akka"   %% "akka-stream-testkit" % V.akka,
    "org.asynchttpclient" % "async-http-client"    % "2.4.7"
  )

  val orientdb = Seq(
    "com.orientechnologies" % "orientdb-graphdb" % "3.0.1"
  )
}
