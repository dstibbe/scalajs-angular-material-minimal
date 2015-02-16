import PlayKeys._
import EclipseKeys._
import play.twirl.sbt.Import.TwirlKeys._

import com.typesafe.sbt.packager.universal.Keys.dist
import com.typesafe.sbt.packager.Keys.stage

name := "scalajs-angular-material-start"

scalaVersion in ThisBuild := "2.11.5"

scalacOptions in ThisBuild ++= Seq("-deprecation", "-unchecked", "-feature")

val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")

val crossTargetSettings = 
  Seq(
    packageJSDependencies,
    fastOptJS, 
    fullOptJS)
  .map {
    task => crossTarget in (client, Compile, task) := scalajsOutputDir.value
  }

resolvers in ThisBuild += Resolver.sonatypeRepo("snapshots")

resolvers in ThisBuild += "Local Maven Repository" at "file:///"+Path.userHome+"/.m2/repository"

lazy val server = (project in file("scalajvm"))
  .enablePlugins(PlayScala)
  .enablePlugins(SbtWeb)
  .settings(
    name := "example-server",
    scalajsOutputDir := (crossTarget in Compile).value / "classes" / "public" / "js",
    compile in Compile <<= (compile in Compile) dependsOn (fastOptJS in (client, Compile)),
    stage <<= stage dependsOn (fullOptJS in (client, Compile)),
    dist <<= dist dependsOn (fullOptJS in (client, Compile)),
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-jdbc" % "2.3.7",
      "com.h2database" % "h2" % "1.4.185",
      "com.jsuereth" %% "scala-arm" % "1.4",
      "com.github.benhutchison" %% "prickle" % "1.1.3",
      "org.webjars" % "angular-material" % "0.7.1-SNAPSHOT"
    ),
    skipParents in ThisBuild := false)
  .settings(crossTargetSettings: _*)
  .dependsOn(common)
  .aggregate(client)

lazy val client = (project in file("scalajs"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "example-client",
    unmanagedSourceDirectories in Compile := (scalaSource in Compile).value ::(scalaSource in Test).value :: Nil,
    unmanagedSourceDirectories in Test := (scalaSource in Test).value :: Nil,
    libraryDependencies ++= Seq(
      "com.greencatsoft" %%% "scalajs-angular" % "0.4-SNAPSHOT",
      "org.scala-js" %%% "scalajs-dom" % "0.8.0",
      "be.doeraene" %%% "scalajs-jquery" % "0.8.0",
      "com.github.benhutchison" %%% "prickle" % "1.1.3",
      "com.lihaoyi" %%% "scalatags" % "0.4.5"),
    jsDependencies ++= Seq(
      "org.webjars" % "jquery" % "2.1.3" / "jquery.min.js",
      "org.webjars" % "angularjs" % "1.3.11" / "angular.js" dependsOn "angular-file-upload-shim.js",
      "org.webjars" % "angularjs" % "1.3.11" / "angular-route.js" dependsOn "angular.js",
      "org.webjars" % "angularjs" % "1.3.11" / "angular-animate.js" dependsOn "angular.js",
      "org.webjars" % "angularjs" % "1.3.11" / "angular-aria.js" dependsOn "angular.js",
      "org.webjars" % "angularjs" % "1.3.11" / "angular-locale_ko.js" dependsOn "angular.js",
      "org.webjars" % "angular-material" % "0.7.1-SNAPSHOT" / "angular-material.js" dependsOn "angular.js",
      "org.webjars" % "angular-file-upload" % "3.0.2" / "angular-file-upload-shim.js" dependsOn "jquery.min.js",
      "org.webjars" % "angular-file-upload" % "3.0.2" / "angular-file-upload.js" dependsOn "angular.js",
      RuntimeDOM),
    skip in packageJSDependencies := false,
    relativeSourceMaps := true
)
  .dependsOn(common)

lazy val common = (project in file("scala"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "example-common",
    unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil,
    unmanagedSourceDirectories in Test := (scalaSource in Test).value :: Nil,
    libraryDependencies ++= Seq(
      "org.scala-js" %% "scalajs-stubs" % scalaJSVersion),
    relativeSourceMaps := true
)
