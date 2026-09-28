import Settings.project

inThisBuild(
  List(
    scalaVersion      := ScalaVersions.scala3,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    organization      := "org.virtuslab",
    homepage          := Some(uri("https://github.com/VirtusLab/scala-packager")),
    licenses          := List(
      "Apache-2.0" -> uri("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "lwronski",
        "Łukasz Wroński",
        "",
        uri("https://github.com/lwronski")
      ),
      Developer(
        "Gedochao",
        "Piotr Chabelski",
        "pchabelski@virtuslab.com",
        uri("https://github.com/Gedochao")
      ),
      Developer(
        "tgodzik",
        "Tomasz Godzik",
        "tgodzik@virtuslab.com",
        uri("https://github.com/tgodzik")
      )
    )
  )
)

lazy val coreDependencies = Seq(
  libraryDependencies ++= Seq(
    Deps.commonsIo,
    Deps.jib,
    Deps.osLib
  )
)

lazy val imageResizerDependencies = Seq(
  libraryDependencies ++= Seq(
    Deps.image4j,
    Deps.thumbnailator
  )
)

lazy val testFramework = Seq(
  testFrameworks += new TestFramework("munit.Framework")
)

lazy val cliMainClass = Seq(
  Compile / mainClass := Some("packager.cli.PackagerCli")
)

lazy val compileOptions: Seq[Setting[?]] = Seq(
  scalacOptions ++= Seq("-Werror", "-deprecation", "-Wunused:all")
)

lazy val packagerProjectSettings = Seq(
  name         := "scala-packager",
  scalaVersion := ScalaVersions.scala3
)

lazy val imageResizerProjectSettings = Seq(
  name         := "scala-packager-image-resizer",
  scalaVersion := ScalaVersions.scala3
)

lazy val cliProjectSettings = Seq(
  name         := "scala-packager-cli",
  scalaVersion := ScalaVersions.scala3,
  libraryDependencies ++= Seq(Deps.caseApp)
)

lazy val utest: Seq[Setting[?]] = Seq(
  libraryDependencies ++= Seq(Deps.munit % Test, Deps.expecty % Test),
  testFrameworks += new TestFramework("munit.Framework"),
  scalaVersion := ScalaVersions.scala3
)

lazy val cli = project("cli")
  .dependsOn(packager, `image-resizer`)
  .settings(
    cliProjectSettings,
    cliMainClass,
    utest,
    compileOptions
  )

lazy val packager = project("packager")
  .settings(
    packagerProjectSettings,
    coreDependencies,
    utest,
    compileOptions
  )

lazy val `image-resizer` = project("image-resizer")
  .dependsOn(packager, packager % "test->test")
  .settings(
    imageResizerProjectSettings,
    imageResizerDependencies,
    utest,
    compileOptions
  )
