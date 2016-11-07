enablePlugins(JavaAppPackaging)

scalafmtConfig in ThisBuild := Some(file(".scalafmt.conf"))

lazy val root =
  (project in file(".")).aggregate(server, client).settings(commonSettings: _*)

lazy val commonSettings = Seq(
  // organization := "com.example",
  version := "0.1.0",
  scalaVersion := V.scala,
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-Xfatal-warnings",
    "-language:postfixOps"
  ),
  libraryDependencies ++= Seq(
    "org.scalacheck" %%% "scalacheck" % "1.13.2" % "test",
    "org.scalactic"  %%% "scalactic"  % "3.0.0"  % "test",
    "org.scalatest"  %%% "scalatest"  % "3.0.0"  % "test"
  ),
  test in assembly := {}
)

lazy val itSettings = Defaults.itSettings ++ Seq(
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.13.2" % "it",
      "org.scalactic"  %% "scalactic"  % "3.0.0"  % "it",
      "org.scalatest"  %% "scalatest"  % "3.0.0"  % "it"
    )
  )

lazy val V = new Object {
  val akka    = "2.4.9"
  val scala   = "2.11.8"
  val kamon   = "0.6.3"
  val log4j   = "2.7"
  val jackson = "2.8.4"
}

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .settings(
    assemblyJarName in assembly := "full.jar",
    herokuAppName in Compile := "superskier",
    herokuFatJar in Compile := Some((assemblyOutputPath in assembly).value),
    name := "server",
    javaOptions := Seq(
      "-Xmx500m",
      "-Xms500m",
      "-XX:+UseG1GC",
      "-XX:+UseCompressedOops",
      "-XX:+AggressiveOpts",
      "-XX:MaxGCPauseMillis=10"),
    javaOptions in reStart := javaOptions.value,
    libraryDependencies ++= Seq(
      "com.github.scopt"                 %% "scopt"                  % "3.5.0",
      "org.apache.logging.log4j"         % "log4j-core"              % V.log4j,
      "org.apache.logging.log4j"         % "log4j-slf4j-impl"        % V.log4j,
      "org.apache.logging.log4j"         % "log4j-jul"               % V.log4j,
      "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % V.jackson,
      "com.fasterxml.jackson.core"       % "jackson-databind"        % V.jackson,
      "com.typesafe.scala-logging"       %% "scala-logging"          % "3.5.0",
      "com.typesafe.akka"                %% "akka-actor"             % V.akka,
      "com.typesafe.akka"                %% "akka-stream"            % V.akka,
      "com.typesafe.akka"                %% "akka-http-experimental" % V.akka,
      "com.typesafe.akka"                %% "akka-slf4j"             % V.akka,
      "io.kamon"                         %% "kamon-core"             % V.kamon,
      "io.kamon"                         %% "kamon-system-metrics"   % V.kamon,
      "io.kamon"                         %% "kamon-influxdb"         % V.kamon
    ),
    (resourceGenerators in Compile) += Def.task {
      val f1          = (fastOptJS in Compile in client).value.data
      val f1SourceMap = f1.getParentFile / (f1.getName + ".map")
      val f2          = (packageScalaJSLauncher in Compile in client).value.data
      val f3          = (packageJSDependencies in Compile in client).value
      Seq(f1, f1SourceMap, f2, f3)
    }.taskValue,
    watchSources ++= (watchSources in client).value
  )
  .dependsOn(sharedJvm)
  .enablePlugins(JavaAppPackaging)

lazy val client = (project in file("client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "Sock Client",
    persistLauncher in Compile := true,
    persistLauncher in Test := false,
    libraryDependencies ++= Seq(
      "org.scala-js"     %%% "scalajs-dom" % "0.9.0",
      "org.singlespaced" %%% "scalajs-d3"  % "0.3.3"
    )
  )
  .dependsOn(shared.js)

lazy val load = (project in file("load"))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(itSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-testkit"        % V.akka,
      "com.typesafe.akka" %% "akka-stream-testkit" % V.akka
    )
  )
  .dependsOn(server)
  .dependsOn(sharedJvm)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "upickle"   % "0.4.1",
      "me.chrons"   %%% "boopickle" % "1.2.4"
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs  = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command
  .process("project server", _: State)) compose (onLoad in Global).value
