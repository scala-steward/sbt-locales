import sbt.Keys._

libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "1.2.0")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.2.0")

addSbtPlugin("org.scala-js"     % "sbt-scalajs"      % "1.12.0")
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.7")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")

addSbtPlugin("com.github.sbt"            % "sbt-ci-release" % "1.5.11")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat"   % "0.3.1")
