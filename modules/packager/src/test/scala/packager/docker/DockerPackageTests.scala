package packager.docker

import packager.PackagerHelper
import packager.config.DockerSettings
import com.eed3si9n.expecty.Expecty.expect

import scala.util.Properties

class DockerPackageTests extends munit.FunSuite with PackagerHelper {

  private val qualifier = "latest"
  private val repository = "echo-scala-packager"

  if (Properties.isLinux) {
    test("should build docker image") {
      {
        val dockerPackage = DockerPackage(echoLauncherPath, buildSettings)
        // build docker image
        dockerPackage.build()

        val expectedImage =
          s"$repository:$qualifier"
        val expectedOutput = "echo"

        val output = os
          .proc("docker", "run", expectedImage, expectedOutput)
          .call(cwd = os.root)
          .out
          .text()
          .trim

        expect(output == expectedOutput)

        // clear
        os.proc("docker", "rmi", "-f", expectedImage).call(cwd = os.root)
      }
    }
  }

  override def buildSettings: DockerSettings =
    DockerSettings(
      from = "adoptopenjdk/openjdk8",
      registry = None,
      repository = repository,
      tag = Some(qualifier),
      exec = "sh"
    )

}
