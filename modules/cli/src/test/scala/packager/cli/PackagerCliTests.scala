package packager.cli

import com.eed3si9n.expecty.Expecty.expect
import packager.cli.commands.DockerOptions

class PackagerCliTests extends munit.FunSuite {
  test("docker options should pass exposed ports to docker settings") {
    val args = Seq(
      "--from",
      "eclipse-temurin:17-jre",
      "--repository",
      "repo",
      "--exposed-ports",
      "8080",
      "--exposed-ports",
      "53/udp"
    )
    val parsed = DockerOptions.parser.parse(args)
    expect(parsed.isRight)
    val exposedPorts = parsed.toOption.map(_._1.toDockerSettings.exposedPorts)
    expect(exposedPorts.contains(Seq("8080", "53/udp")))
  }
}
