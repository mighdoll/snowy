import Dependencies._

lazy val root =
  (project in file("."))
    .aggregate(server, client, measures, measuresListener, load, sharedJvm, sharedJs)
    .settings(commonSettings: _*)


lazy val commonSettings = Seq(
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
    scalaCheck % "test",
    scalactic % "test",
    scalaTest % "test"
  ),
  test in assembly := {}
)

lazy val itSettings = Defaults.itSettings ++ Seq(
  libraryDependencies ++= Seq(
    scalaCheck % "it",
    scalactic  % "it",
    scalaTest  % "it"
  )
)

lazy val server = (project in file("server"))
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings: _*)
  .settings(
    assemblyJarName in assembly := "full.jar",
    herokuAppName in Compile := "snowy-3d",
    herokuFatJar in Compile := Some((assemblyOutputPath in assembly).value),
    name := "server",
    javaOptions := Seq(
      "-Xmx500m",
      "-Xms500m",
      "-XX:+UseG1GC",
      "-XX:+UseCompressedOops",
      "-XX:+AggressiveOpts",
      "-XX:MaxGCPauseMillis=10"
    ),
    javaOptions in reStart := javaOptions.value,
    libraryDependencies ++= squants ++ log4j ++ akkaHttp ++ akka ++ cats ++ scopt ++ scalaLogging ++ loggingProvider ++ akkaStreams,
    (resourceGenerators in Compile) += Def.task {
      val f1          = (fastOptJS in Compile in client).value.data
      val f1SourceMap = f1.getParentFile / (f1.getName + ".map")
      val f2          = (packageJSDependencies in Compile in client).value
      Seq(f1, f1SourceMap, f2)
    }.taskValue,
    watchSources ++= (watchSources in client).value
  )
  .dependsOn(sharedJvm, measures)

lazy val client = (project in file("client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "Sock Client",
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= scalaJSDom,
    jsDependencies ++= jsLibraries
  )
  .dependsOn(shared.js)

lazy val measures = (project in file("measures"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= scalaLogging ++ akkaStreams
  )

lazy val measuresListener = (project in file("measures-listener"))
  .settings(commonSettings: _*)
  .settings(
    javaOptions in reStart ++= Seq(
      "-XX:MaxDirectMemorySize=512G",
      "-Xmx3G",
      "-Xms3G"
    ),
    libraryDependencies ++= orientdb ++ scopt ++ scalaLogging ++ loggingProvider ++ akkaStreams
  )
  .dependsOn(measures)

lazy val load = (project in file("load"))
  .enablePlugins(JavaAppPackaging)
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(itSettings: _*)
  .settings(
    libraryDependencies ++= akkaLoad
  )
  .dependsOn(server, sharedJvm, measures)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies += boopickle,
    addCompilerPlugin(macroParadise cross CrossVersion.full)
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs  = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command
  .process("project server", _: State)) compose (onLoad in Global).value
