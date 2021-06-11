
inThisBuild(List(
  organization := "org.virtuslab",
  homepage := Some(url("https://github.com/VirtuslabRnD/scala-packager")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "lwronski",
      "Łukasz Wroński",
      "",
      url("https://github.com/lwronski")
    )
  )
))

scalaVersion := ScalaVersions.scala213
crossScalaVersions := ScalaVersions.all

libraryDependencies ++= Seq(
  Deps.osLib,
  Deps.munit % Test,
  Deps.expecty % Test
)

testFrameworks += new TestFramework("munit.Framework")
