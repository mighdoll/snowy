import Dependencies._
import sbtcrossproject.{crossProject, CrossType}

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
    "-language:postfixOps",
    "-Ypartial-unification"
  ),
  libraryDependencies ++= Seq(
    scalaCheck % "test",
    scalactic  % "test",
    scalaTest  % "test"
  ),
)

lazy val itSettings = Defaults.itSettings ++ Seq(
  libraryDependencies ++= Seq(
    scalaCheck % "it",
    scalactic  % "it",
    scalaTest  % "it"
  )
)

lazy val server = (project in file("server"))
  .enablePlugins(SbtWeb, JavaAppPackaging)
  .settings(commonSettings: _*)
  .settings(
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
    libraryDependencies ++= squants ++ akka ++ cats ++ scopt ++ scalaLogging ++ akkaStreams,
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value,

    (resourceGenerators in Compile) += Def.task {
      val f1          = (webpack in fastOptJS in Compile in client).value.head.data
      val f1SourceMap = f1.getParentFile / (f1.getName + ".map")
      val f2          = (packageJSDependencies in Compile in client).value
      Seq(f1, f1SourceMap, f2)
    }.taskValue,
    watchSources ++= (watchSources in client).value
  )
  .dependsOn(sharedJvm, measures)

lazy val client = (project in file("client"))
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin, ScalaJSWeb)
  .settings(commonSettings: _*)
  .settings(
    name := "Sock Client",
    scalaJSUseMainModuleInitializer := true,
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    libraryDependencies ++= scalaJSDomSetting.value,
    jsDependencies ++= jsLibraries,
    npmDependencies in Compile ++= Seq(
      "three" -> "0.92.0"
    )
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
    libraryDependencies ++= orientdb ++ scopt ++ scalaLogging ++ akkaStreams
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

lazy val shared = (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= booPickleSetting.value
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs  = shared.js

// loads the server project at sbt startup
onLoad in Global := {
  val initCommands = { state: State =>
    "dependencyUpdates" :: "project server" :: state
  }
  initCommands compose (onLoad in Global).value
}
