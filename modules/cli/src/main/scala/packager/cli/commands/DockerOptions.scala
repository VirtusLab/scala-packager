package packager.cli.commands

import caseapp.*
import caseapp.core.help.Help
import packager.cli.commands.SettingsHelpers.Mandatory
import packager.config.DockerSettings

import java.nio.file.{Path, Paths}

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
  dockerExecutable: Option[Path] = Some(Paths.get("docker")),
  @Group("Docker")
  @HelpMessage(
    "extra directories to be added to the image"
  )
  extraDirectories: List[Path] = Nil
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
      dockerExecutable = dockerExecutable,
      extraDirectories = extraDirectories
    )
}

case object DockerOptions {
  implicit lazy val parser: Parser[DockerOptions] = Parser.derive
  implicit lazy val help: Help[DockerOptions]     = Help.derive
}
