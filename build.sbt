import sbt._
import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }

Global / onChangedBuildSource := ReloadOnSourceChanges

pluginCrossBuild / sbtVersion := "1.2.8"

inThisBuild(
  List(
    organization := "io.github.cquiroz",
    homepage := Some(url("https://github.com/cquiroz/sbt-locales")),
    licenses := Seq("BSD 3-Clause License" -> url("https://opensource.org/licenses/BSD-3-Clause")),
    developers := List(
      Developer("cquiroz",
                "Carlos Quiroz",
                "carlos.m.quiroz@gmail.com",
                url("https://github.com/cquiroz")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/cquiroz/sbt-locales"),
        "scm:git:git@github.com:cquiroz/sbt-locales.git"
      )
    )
  )
)

lazy val commonSettings = Seq(
  name := "sbt-locales",
  scalaVersion := "2.12.14",
  javaOptions ++= Seq("-Dfile.encoding=UTF8"),
  autoAPIMappings := true,
  resolvers += "Sonatype OSS Snapshots".at( // TODO: remove
    "https://oss.sonatype.org/content/repositories/snapshots"
  )
)

lazy val api = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("api"))
  .settings(commonSettings: _*)
  .settings(
    name := "cldr-api",
    scalaVersion := "2.12.14", // needs to match the version for sbt
    description := "scala-java-locales cldrl api",
    crossScalaVersions := Seq("2.11.12", "2.12.14", "2.13.4", "3.1.2"),
    libraryDependencies ++= List(
      "org.scalameta" %%% "munit" % "1.0.0-M3+39-d7ab5753-SNAPSHOT" % Test
    ),
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies += {
      // workaround for https://github.com/scala-native/scala-native/issues/2546
      if (scalaVersion.value.startsWith("3.") && crossProjectPlatform.value.identifier == "native")
        ("org.portable-scala"   % "portable-scala-reflect_native0.4_2.13" % "1.1.1")
          .excludeAll(
            ExclusionRule(organization = "org.scala-native")
          )
      else
        ("org.portable-scala" %%% "portable-scala-reflect"                % "1.1.1")
          .cross(CrossVersion.for3Use2_13)
    }
  )
  .jsSettings(scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))

lazy val sbt_locales = project
  .in(file("sbt-locales"))
  .enablePlugins(SbtPlugin)
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "sbt-locales",
    description := "Sbt plugin to build custom locale databases",
    scalaVersion := "2.12.14",
    crossScalaVersions := Seq(),
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    Compile / resources ++= (api.jvm / Compile / sources).value,
    scriptedBufferLog := false,
    libraryDependencies ++= Seq(
      "com.eed3si9n"           %% "gigahorse-okhttp" % "0.6.0",
      "org.scala-lang.modules" %% "scala-xml"        % "2.1.0",
      "org.typelevel"          %% "cats-core"        % "2.7.0",
      "org.typelevel"          %% "cats-effect"      % "2.5.4",
      "com.eed3si9n"           %% "treehugger"       % "0.4.4"
    )
  )
  .dependsOn(api.jvm)

lazy val root = project
  .in(file("."))
  .settings(
    publish := {},
    publishLocal := {},
    publishArtifact := false
  )
  .aggregate(api.js, api.jvm, api.native, sbt_locales)
