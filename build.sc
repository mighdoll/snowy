import mill._, scalalib._
import mill.scalajslib._
import ammonite.ops._

object client extends ScalaJSModule {
  def scalaVersion = "2.12.12"
  def scalaJSVersion = "0.6.33"
  def moduleDeps = Seq(shared)
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.14.3",
    ivy"org.scalactic::scalactic::3.2.2",
    ivy"org.scalatest::scalatest::3.2.2",
    ivy"org.scala-js::scalajs-dom::1.1.0",
  )
  def scalacOptions = Seq("-feature", "-deprecation")
}

object shared extends ScalaModule with ScalaJSModule {
  def scalaVersion = "2.12.12"
  def scalaJSVersion = "0.6.33"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.14.3",
    ivy"org.scalactic::scalactic::3.2.2",
    ivy"org.scalatest::scalatest::3.2.2",
    ivy"io.suzaku::boopickle::1.3.3",
  )
  def scalacOptions = Seq("-feature", "-deprecation")
}

object server extends ScalaModule {
  def moduleDeps = Seq(shared, measures)
  def scalaVersion = "2.12.12"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.14.3",
    ivy"org.scalactic::scalactic::3.2.2",
    ivy"org.scalatest::scalatest::3.2.2",
    ivy"io.suzaku::boopickle::1.3.3",
    ivy"org.typelevel::squants::1.7.0",
    ivy"com.typesafe.akka::akka-http::10.2.1",
    ivy"org.typelevel::cats-core::2.2.0",
    ivy"com.github.scopt::scopt::3.7.1",
    ivy"com.outr::scribe::2.8.6",
    ivy"com.typesafe.akka::akka-actor::2.6.10",
    ivy"com.typesafe.akka::akka-stream::2.6.10"
  )
  def resources = T.sources (
    millSourcePath / "resources",
    client.fastOpt().path / RelPath.up,
  )
  def scalacOptions = Seq("-feature", "-deprecation")
}

object load extends ScalaModule {
  def moduleDeps = Seq(server, shared, measures)
  def scalaVersion = "2.12.12"
  def ivyDeps = Agg(
    ivy"com.typesafe.akka::akka-testkit::2.6.10",
    ivy"com.typesafe.akka::akka-stream-testkit::2.6.10",
    ivy"org.asynchttpclient:async-http-client:2.12.1"
  )
  def scalacOptions = Seq("-feature", "-deprecation")
}
object measures extends ScalaModule {
  def moduleDeps = Seq(shared)
  def scalaVersion = "2.12.12"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.14.3",
    ivy"org.scalactic::scalactic::3.2.2",
    ivy"org.scalatest::scalatest::3.2.2",
    ivy"com.outr::scribe::2.8.6",
    ivy"com.typesafe.akka::akka-actor::2.6.10",
    ivy"com.typesafe.akka::akka-stream::2.6.10"
  )
  def scalacOptions = Seq("-feature", "-deprecation")
}

object `measures-listener` extends ScalaModule {
  def moduleDeps = Seq(measures)
  def scalaVersion = "2.12.12"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.14.3",
    ivy"org.scalactic::scalactic::3.2.2",
    ivy"org.scalatest::scalatest::3.2.2",
    ivy"com.orientechnologies:orientdb-graphdb:3.1.4",
    ivy"com.github.scopt::scopt::3.7.1",
    ivy"com.outr::scribe::2.8.6",
    ivy"com.typesafe.akka::akka-actor::2.6.10",
    ivy"com.typesafe.akka::akka-stream::2.6.10"
  )
  def scalacOptions = Seq("-feature", "-deprecation")
}