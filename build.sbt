import PlayKeys._

import com.typesafe.sbt.packager.universal.Keys.dist
import com.typesafe.sbt.packager.Keys.stage

name := "scalajs-angular-material-start"

scalaVersion in ThisBuild := "2.11.6"

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
      "org.webjars" % "angular-material" % "0.7.1"
    ))
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
      "com.greencatsoft" %%% "scalajs-angular" % "0.6"),
    jsDependencies ++= Seq(
      "org.webjars" % "angularjs" % "1.3.15" / "angular.js",
      "org.webjars" % "angularjs" % "1.3.15" / "angular-route.js" dependsOn "angular.js",
      "org.webjars" % "angularjs" % "1.3.15" / "angular-animate.js" dependsOn "angular.js",
      "org.webjars" % "angularjs" % "1.3.15" / "angular-aria.js" dependsOn "angular.js",
      "org.webjars" % "angularjs" % "1.3.15" / "angular-locale_ko.js" dependsOn "angular.js",
      "org.webjars" % "angular-material" % "0.7.1" / "angular-material.js" dependsOn "angular.js",
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

commands += Command.command("playme")((state: State) => {
  var newState = state
  //newState = Command.process("clean", newState)
  newState = Command.process("compile", newState)
  newState = Command.process("project server", newState)
  newState = Command.process("run", newState)
  newState
})