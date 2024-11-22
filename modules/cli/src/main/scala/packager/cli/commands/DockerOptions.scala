package packager.cli.commands

import caseapp.core.help.Help
import caseapp.{Group, HelpMessage, Parser, ValueDescription}
import packager.cli.commands.SettingsHelpers.Mandatory
import packager.config.DockerSettings
import java.nio.file.Path;
import java.nio.file.Paths;

final case class DockerOptions(
  @Group("Docker")
  @HelpMessage(
    "Building the container from base image"
  )
  @ValueDescription("ubuntu|ubuntu:latest|adoptopenjdk/openjdk8:debian-jre|…")
  from: Option[String] = None,
  @Group("Docker")
  @HelpMessage(
    "The image registry, if empty the default registry will be used (Docker Hub)"
  )
  registry: Option[String] = None,
  @Group("Docker")
  @HelpMessage(
    "The image repository"
  )
  repository: Option[String] = None,
  @Group("Docker")
  @HelpMessage(
    "The image tag, the default tag is latest"
  )
  tag: Option[String] = None,
  @Group("Docker")
  @HelpMessage(
    "Executable that will run an application, default sh"
  )
  exec: Option[String] = Some("sh"),
  @Group("Docker")
  @HelpMessage(
    "docker executable that will be used. ex. docker, podman"
  )
  @ValueDescription("docker")
  dockerExecutable: Option[Path] = Some(Paths.get("docker"))
) {
  def toDockerSettings: DockerSettings =
    DockerSettings(
      from = from.mandatory(
        "Maintainer parameter is mandatory for docker image"
      ),
      registry = registry,
      repository = repository.mandatory(
        "Repository parameter is mandatory for docker image"
      ),
      tag = tag,
      exec = exec,
      dockerExecutable = dockerExecutable
    )
}

case object DockerOptions {

  lazy val parser: Parser[DockerOptions]                           = Parser.derive
  implicit lazy val parserAux: Parser.Aux[DockerOptions, parser.D] = parser
  implicit lazy val help: Help[DockerOptions]                      = Help.derive

}
