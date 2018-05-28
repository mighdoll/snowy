addSbtPlugin("io.get-coursier"  % "sbt-coursier"        % "1.0.3")
addSbtPlugin("com.timushev.sbt" % "sbt-updates"         % "0.3.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.4")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.4.0")
addSbtPlugin("ch.epfl.scala"    % "sbt-scalajs-bundler" % "0.12.0")
addSbtPlugin("ch.epfl.scala"    % "sbt-web-scalajs-bundler" % "0.12.0")
addSbtPlugin("com.vmunier"      % "sbt-web-scalajs" % "1.0.7")
addSbtPlugin("org.scala-js"     % "sbt-scalajs"         % "0.6.23")
addSbtPlugin("io.spray"         % "sbt-revolver"        % "0.9.1")
addSbtPlugin("ch.epfl.scala"    % "sbt-bloop"           % "1.0.0-M9")

classpathTypes += "maven-plugin"