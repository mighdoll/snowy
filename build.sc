import $ivy.`com.lihaoyi::mill-contrib-bloop:0.11.1`
import $ivy.`com.goyeau::mill-scalafix::0.3.1`
import com.goyeau.mill.scalafix.ScalafixModule
import mill._, scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

object client extends ScalaJSModule with ScalafixModule {
  def scalaVersion = "2.12.18"
  def scalaJSVersion = "1.13.2"
  def moduleDeps = Seq(shared)
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.17.0",
    ivy"org.scalactic::scalactic::3.2.16",
    ivy"org.scalatest::scalatest::3.2.16",
    ivy"org.scala-js::scalajs-dom::2.6.0",
  )
  def moduleKind = T(ModuleKind.ESModule)

  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}

object shared extends ScalaModule with ScalaJSModule with ScalafixModule {
  def scalaVersion = "2.12.18"
  def scalaJSVersion = "1.13.2"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.17.0",
    ivy"org.scalactic::scalactic::3.2.16",
    ivy"org.scalatest::scalatest::3.2.16",
    ivy"io.suzaku::boopickle::1.4.0",
  )
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}

object server extends ScalaModule with ScalafixModule {
  def moduleDeps = Seq(shared, measures)
  def scalaVersion = "2.12.18"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.17.0",
    ivy"org.scalactic::scalactic::3.2.16",
    ivy"org.scalatest::scalatest::3.2.16",
    ivy"io.suzaku::boopickle::1.4.0",
    ivy"org.typelevel::squants::1.8.3",
    ivy"com.typesafe.akka::akka-http::10.5.2",
    ivy"org.typelevel::cats-core::2.9.0",
    ivy"com.github.scopt::scopt::4.1.0",
    ivy"com.outr::scribe::3.11.8",
    ivy"com.typesafe.akka::akka-actor::2.8.3",
    ivy"com.typesafe.akka::akka-stream::2.8.3"
  )
  def resources = T {
    os.makeDir(T.dest / "web")

    val jsPath = client.fastLinkJS().dest.path

    os.copy(jsPath / "main.js", T.dest / "web" / "main.js")
    os.copy(jsPath / "main.js.map", T.dest / "web" / "main.js.map")

    super.resources() ++ Seq(PathRef(T.dest))
  }
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}

object load extends ScalaModule with ScalafixModule {
  def moduleDeps = Seq(server, shared, measures)
  def scalaVersion = "2.12.18"
  def ivyDeps = Agg(
    ivy"com.typesafe.akka::akka-testkit::2.8.3",
    ivy"com.typesafe.akka::akka-stream-testkit::2.8.3",
    ivy"org.asynchttpclient:async-http-client:2.12.3"
  )
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}
object measures extends ScalaModule with ScalafixModule {
  def moduleDeps = Seq(shared)
  def scalaVersion = "2.12.18"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.17.0",
    ivy"org.scalactic::scalactic::3.2.16",
    ivy"org.scalatest::scalatest::3.2.16",
    ivy"com.outr::scribe::3.11.8",
    ivy"com.typesafe.akka::akka-actor::2.8.3",
    ivy"com.typesafe.akka::akka-stream::2.8.3"
  )
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}

object `measures-listener` extends ScalaModule with ScalafixModule {
  def moduleDeps = Seq(measures)
  def scalaVersion = "2.12.18"
  def ivyDeps = Agg(
    ivy"org.scalacheck::scalacheck::1.17.0",
    ivy"org.scalactic::scalactic::3.2.16",
    ivy"org.scalatest::scalatest::3.2.16",
    ivy"com.orientechnologies:orientdb-graphdb:3.2.21",
    ivy"com.github.scopt::scopt::4.1.0",
    ivy"com.outr::scribe::3.11.8",
    ivy"com.typesafe.akka::akka-actor::2.8.3",
    ivy"com.typesafe.akka::akka-stream::2.8.3"
  )
  def scalacOptions = Seq("-feature", "-deprecation", "-Ywarn-unused")
}