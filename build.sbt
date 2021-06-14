import Settings.project

inThisBuild(
  List(
    organization := "org.virtuslab",
    homepage := Some(url("https://github.com/VirtuslabRnD/scala-packager")),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "lwronski",
        "Łukasz Wroński",
        "",
        url("https://github.com/lwronski")
      )
    )
  )
)

scalacOptions := Seq("-unchecked", "-deprecation")

lazy val coreDependencies = Seq(
  libraryDependencies ++= Seq(
    Deps.osLib,
    Deps.caseApp,
    Deps.munit % Test,
    Deps.expecty % Test
  )
)

lazy val testFramework = Seq(
  testFrameworks += new TestFramework("munit.Framework")
)

lazy val cliMainClass = Seq(
  Compile / mainClass := Some("cli.PackagerCli")
)

lazy val packagerName = Seq(
  name := "scala-packager",
  scalaVersion := ScalaVersions.scala213,
  crossScalaVersions := ScalaVersions.all
)

lazy val cliName = Seq(
  name := "scala-packager-cli",
  scalaVersion := ScalaVersions.scala213,
  crossScalaVersions := ScalaVersions.all
)

lazy val cli = project("cli")
  .dependsOn(packager)
  .settings(cliName)
  .settings(coreDependencies)
  .settings(testFramework)
  .settings(cliMainClass)


lazy val packager = project("packager")
  .settings(packagerName)
  .settings(coreDependencies)
  .settings(testFramework)

