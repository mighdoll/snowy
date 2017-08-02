import sbt._

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

  val loggingProvider = Seq(
    "org.apache.logging.log4j"         % "log4j-core"              % V.log4j,
    "org.apache.logging.log4j"         % "log4j-slf4j-impl"        % V.log4j,
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % V.jackson,
    "com.fasterxml.jackson.core"       % "jackson-databind"        % V.jackson
  )

}

