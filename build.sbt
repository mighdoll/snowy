enablePlugins(JavaAppPackaging)

lazy val root =
  (project in file(".")).aggregate(server, client).settings(commonSettings: _*)

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
    "org.scalacheck" %%% "scalacheck" % V.scalacheck % "test",
    "org.scalactic"  %%% "scalactic"  % V.scalactic  % "test",
    "org.scalatest"  %%% "scalatest"  % V.scalatest  % "test"
  ),
  test in assembly := {}
)

lazy val itSettings = Defaults.itSettings ++ Seq(
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % V.scalacheck % "it",
      "org.scalactic"  %% "scalactic"  % V.scalactic  % "it",
      "org.scalatest"  %% "scalatest"  % V.scalatest  % "it"
    )
  )

lazy val V = new Object {
  val scala      = "2.12.2"
  val akka       = "2.5.0"
  val akkaHttp   = "10.0.5"
  val log4j      = "2.8.2"
  val jackson    = "2.8.8"
  val scalacheck = "1.13.5"
  val scalactic  = "3.0.3"
  val scalatest  = "3.0.3"
}

lazy val server = (project in file("server"))
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
      "com.typesafe.akka"                %% "akka-http"              % V.akkaHttp,
      "com.typesafe.akka"                %% "akka-slf4j"             % V.akka,
      "org.typelevel"                    %% "cats"                   % "0.9.0"
    ),
    (resourceGenerators in Compile) += Def.task {
      val f1          = (fastOptJS in Compile in client).value.data
      val f1SourceMap = f1.getParentFile / (f1.getName + ".map")
      val f2          = (packageJSDependencies in Compile in client).value
      Seq(f1, f1SourceMap, f2)
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
    scalaJSUseMainModuleInitializer := true,
    resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1"
    ),
    jsDependencies ++= Seq(
      "org.webjars.npm"   % "three"                      % "0.84.0" / "0.84.0/build/three.min.js",
      "org.webjars.bower" % "github-com-mrdoob-stats-js" % "r17" / "r17/build/stats.min.js"
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
      "me.chrons" %%% "boopickle" % "1.2.5"
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs  = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command
  .process("project server", _: State)) compose (onLoad in Global).value
