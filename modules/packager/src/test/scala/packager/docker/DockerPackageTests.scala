package packager.docker

import com.eed3si9n.expecty.Expecty.expect
import packager.PackagerHelper
import packager.config.DockerSettings

import java.nio.file.Paths

import scala.util.Properties

class DockerPackageTests extends munit.FunSuite with PackagerHelper {

  private val qualifier  = "latest"
  private val repository = "scalafmt-scala-packager"

  if (Properties.isLinux) {
    test("should build docker image") {
      val dockerPackage = DockerPackage(scalafmtLauncherPath, buildSettings)
      // build docker image
      dockerPackage.build()

      val expectedImage  = s"$repository:$qualifier"
      val expectedOutput = s"scalafmt $scalafmtVersion"

      val output = os
        .proc("docker", "run", expectedImage, "--version")
        .call(cwd = os.root)
        .out
        .text()
        .trim

      expect(output == expectedOutput)

      // clear
      os.proc("docker", "rmi", "-f", expectedImage).call(cwd = os.root)
    }
    test("should build docker image with native application") {
      val nativeAppSettings = buildSettings.copy(exec = None)
      val dockerPackage     = DockerPackage(scalafmtNativePath, nativeAppSettings)
      // build docker image
      dockerPackage.build()

      val expectedImage  = s"$repository:$qualifier"
      val expectedOutput = s"scalafmt $scalafmtVersion"

      val output = os
        .proc("docker", "run", expectedImage, "--version")
        .call(cwd = os.root)
        .out
        .text()
        .trim

      expect(output == expectedOutput)

      // clear
      os.proc("docker", "rmi", "-f", expectedImage).call(cwd = os.root)
    }
    test("should build docker image with additional directories") {
      val extraDir  = os.temp.dir(prefix = "extra-")
      val extraFile = extraDir / "main.scala"
      os.write(
        extraFile,
        s"""@main def main(): Unit = println("Hello World")
           |""".stripMargin
      )
      val config =
        s"""version = $scalafmtVersion
           |runner.dialect = scala3""".stripMargin

      val appSettings = buildSettings.copy(
        extraDirectories = List(extraDir.toNIO)
      )
      val dockerPackage = DockerPackage(scalafmtLauncherPath, appSettings)
      // build docker image
      dockerPackage.build()

      val expectedImage  = s"$repository:$qualifier"
      val expectedOutput = "All files are formatted with scalafmt :)"

      val output = os
        .proc("docker", "run", expectedImage, "--config-str", config, "--test", "/")
        .call(cwd = os.root)
        .out
        .text()
        .trim

      expect(output == expectedOutput)

      // clear
      os.proc("docker", "rmi", "-f", expectedImage).call(cwd = os.root)
    }
  }

  override def buildSettings: DockerSettings =
    DockerSettings(
      from = "eclipse-temurin:8-jdk",
      registry = None,
      repository = repository,
      tag = Some(qualifier),
      exec = Some("sh"),
      dockerExecutable = Some(Paths.get("docker")),
      extraDirectories = Nil
    )

}
