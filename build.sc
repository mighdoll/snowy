import $ivy.`com.goyeau::mill-scalafix:0.2.5`
import $ivy.`com.lihaoyi::mill-contrib-bloop:0.9.9`
import com.goyeau.mill.scalafix.ScalafixModule
import mill._, scalalib._
import mill.scalajslib._
import ammonite.ops._

object client extends ScalaJSModule with ScalafixModule {
  def scalaVersion = "2.12.14"
  def scalaJSVersion = "1.6.0"
  def moduleDeps = Seq(shared)
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.15.4",
    ivy"org.scalactic::scalactic::3.2.9",
    ivy"org.scalatest::scalatest::3.2.9",
    ivy"org.scala-js::scalajs-dom::1.1.0",
  )
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}

object shared extends ScalaModule with ScalaJSModule with ScalafixModule {
  def scalaVersion = "2.12.14"
  def scalaJSVersion = "1.6.0"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.15.4",
    ivy"org.scalactic::scalactic::3.2.9",
    ivy"org.scalatest::scalatest::3.2.9",
    ivy"io.suzaku::boopickle::1.4.0",
  )
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}

object server extends ScalaModule with ScalafixModule {
  def moduleDeps = Seq(shared, measures)
  def scalaVersion = "2.12.14"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.15.4",
    ivy"org.scalactic::scalactic::3.2.9",
    ivy"org.scalatest::scalatest::3.2.9",
    ivy"io.suzaku::boopickle::1.4.0",
    ivy"org.typelevel::squants::1.7.0",
    ivy"com.typesafe.akka::akka-http::10.2.1",
    ivy"org.typelevel::cats-core::2.6.1",
    ivy"com.github.scopt::scopt::4.0.1",
    ivy"com.outr::scribe::3.5.5",
    ivy"com.typesafe.akka::akka-actor::2.6.10",
    ivy"com.typesafe.akka::akka-stream::2.6.10"
  )
  def resources = T.sources (
    millSourcePath / "resources",
    client.fastOpt().path / RelPath.up,
  )
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}

object load extends ScalaModule with ScalafixModule {
  def moduleDeps = Seq(server, shared, measures)
  def scalaVersion = "2.12.14"
  def ivyDeps = Agg(
    ivy"com.typesafe.akka::akka-testkit::2.6.10",
    ivy"com.typesafe.akka::akka-stream-testkit::2.6.10",
    ivy"org.asynchttpclient:async-http-client:2.12.3"
  )
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}
object measures extends ScalaModule with ScalafixModule {
  def moduleDeps = Seq(shared)
  def scalaVersion = "2.12.14"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.15.4",
    ivy"org.scalactic::scalactic::3.2.9",
    ivy"org.scalatest::scalatest::3.2.9",
    ivy"com.outr::scribe::3.5.5",
    ivy"com.typesafe.akka::akka-actor::2.6.10",
    ivy"com.typesafe.akka::akka-stream::2.6.10"
  )
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}

object `measures-listener` extends ScalaModule with ScalafixModule {
  def moduleDeps = Seq(measures)
  def scalaVersion = "2.12.14"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.15.4",
    ivy"org.scalactic::scalactic::3.2.9",
    ivy"org.scalatest::scalatest::3.2.9",
    ivy"com.orientechnologies:orientdb-graphdb:3.2.0",
    ivy"com.github.scopt::scopt::4.0.1",
    ivy"com.outr::scribe::3.5.5",
    ivy"com.typesafe.akka::akka-actor::2.6.10",
    ivy"com.typesafe.akka::akka-stream::2.6.10"
  )
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}