addSbtPlugin(
  "io.github.cquiroz"             % "sbt-locales"                   % sys.props
    .getOrElse("plugin.version", sys.error("'plugin.version' environment variable is not set"))
)
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "1.3.2")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.2")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"                   % "1.16.0")
addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % "0.4.17")
addSbtPlugin("org.typelevel"      % "sbt-tpolecat"                  % "0.5.2")
