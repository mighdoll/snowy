addSbtPlugin("io.get-coursier"  % "sbt-coursier"        % "1.1.0-M4")
addSbtPlugin("com.timushev.sbt" % "sbt-updates"         % "0.3.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.5")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.5.0")
addSbtPlugin("ch.epfl.scala"    % "sbt-scalajs-bundler" % "0.13.0")
addSbtPlugin("ch.epfl.scala"    % "sbt-web-scalajs-bundler" % "0.13.0")
addSbtPlugin("com.vmunier"      % "sbt-web-scalajs" % "1.0.7")
addSbtPlugin("org.scala-js"     % "sbt-scalajs"         % "0.6.24")
addSbtPlugin("io.spray"         % "sbt-revolver"        % "0.9.1")
addSbtPlugin("ch.epfl.scala"    % "sbt-bloop"           % "1.0.0-M10")

classpathTypes += "maven-plugin"