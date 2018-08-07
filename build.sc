import $ivy.`ch.epfl.scala::mill-bloop:1.0.0`

import mill._, scalalib._
import mill.scalajslib._
import ammonite.ops._

object client extends ScalaJSModule {
  def scalaVersion = "2.12.6"
  def scalaJSVersion = "0.6.24"
  def moduleDeps = Seq(shared)
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.14.0",
    ivy"org.scalactic::scalactic::3.0.5",
    ivy"org.scalatest::scalatest::3.0.5",
    ivy"org.scala-js::scalajs-dom::0.9.6",
  )
}

object shared extends ScalaModule with ScalaJSModule {
  def scalaVersion = "2.12.6"
  def scalaJSVersion = "0.6.24"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.14.0",
    ivy"org.scalactic::scalactic::3.0.5",
    ivy"org.scalatest::scalatest::3.0.5",
    ivy"io.suzaku::boopickle::1.3.0",
  )
}

object server extends ScalaModule {
  def moduleDeps = Seq(shared, measures)
  def scalaVersion = "2.12.6"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.14.0",
    ivy"org.scalactic::scalactic::3.0.5",
    ivy"org.scalatest::scalatest::3.0.5",
    ivy"io.suzaku::boopickle::1.3.0",
    ivy"org.typelevel::squants::1.3.0",
    ivy"com.typesafe.akka::akka-http::10.1.3",
    ivy"org.typelevel::cats-core::1.2.0",
    ivy"com.github.scopt::scopt::3.7.0",
    ivy"com.outr::scribe::2.5.3",
    ivy"com.typesafe.akka::akka-actor::2.5.14",
    ivy"com.typesafe.akka::akka-stream::2.5.14"
  )
  def resources = T.sources (
    millSourcePath / "resources",
    client.fastOpt().path / RelPath.up,
  )
}

object load extends ScalaModule {
  def moduleDeps = Seq(server, shared, measures)
  def scalaVersion = "2.12.6"
  def ivyDeps = Agg(
    ivy"com.typesafe.akka::akka-testkit::2.5.14",
    ivy"com.typesafe.akka::akka-stream-testkit::2.5.14",
    ivy"org.asynchttpclient:async-http-client:2.5.2"
  )
}
object measures extends ScalaModule {
  def moduleDeps = Seq(shared)
  def scalaVersion = "2.12.6"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.14.0",
    ivy"org.scalactic::scalactic::3.0.5",
    ivy"org.scalatest::scalatest::3.0.5",
    ivy"com.outr::scribe::2.5.3",
    ivy"com.typesafe.akka::akka-actor::2.5.14",
    ivy"com.typesafe.akka::akka-stream::2.5.14"
  )
}

object `measures-listener` extends ScalaModule {
  def moduleDeps = Seq(measures)
  def scalaVersion = "2.12.6"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.14.0",
    ivy"org.scalactic::scalactic::3.0.5",
    ivy"org.scalatest::scalatest::3.0.5",
    ivy"com.orientechnologies:orientdb-graphdb:3.0.5",
    ivy"com.github.scopt::scopt::3.7.0",
    ivy"com.outr::scribe::2.5.3",
    ivy"com.typesafe.akka::akka-actor::2.5.14",
    ivy"com.typesafe.akka::akka-stream::2.5.14"
  )
}