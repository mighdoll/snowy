lazy val root = (project in file(".")).
  aggregate(server, client).
  settings(commonSettings: _*)

lazy val commonSettings = Seq(
  // organization := "com.example",
  version := "0.1.0",
  scalaVersion := V.scala,
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings", "-language:postfixOps")
)

lazy val V = new Object {
  val akka = "2.4.9"
  val circe = "0.4.1"
  val scala = "2.11.8"
}

lazy val server = (project in file("server")).
  settings(commonSettings: _*).
  settings(
    name := "hello",
    libraryDependencies ++=  Seq(
      "com.typesafe.akka" %% "akka-actor" % V.akka,
      "com.typesafe.akka" %% "akka-stream" % V.akka,
      "com.typesafe.akka" %% "akka-http-experimental" % V.akka
    ),
    (resourceGenerators in Compile) += Def.task {
      val f1 = (fastOptJS in Compile in client).value.data
      val f1SourceMap = f1.getParentFile / (f1.getName + ".map")
      val f2 = (packageScalaJSLauncher in Compile in client).value.data
      val f3 = (packageJSDependencies in Compile in client).value
      Seq(f1, f1SourceMap, f2, f3)
    }.taskValue,
    watchSources ++= (watchSources in client).value
  ).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "Sock Client",
    persistLauncher in Compile := true,
    persistLauncher in Test := false,
    scalaJSUseRhino in Global := false,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0"
  ).
  dependsOn(shared.js)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file ("shared")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "upickle" % "0.4.1"
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js
