
scalaVersion := ScalaVersions.scala213
crossScalaVersions := ScalaVersions.all

libraryDependencies ++= Seq(
  Deps.osLib,
  Deps.munit % Test,
  Deps.expecty % Test
)

testFrameworks += new TestFramework("munit.Framework")
