package packager.cli.commands

import caseapp.core.help.Help
import caseapp.{Group, HelpMessage, Parser, ValueDescription}
import packager.cli.commands.SettingsHelpers.Mandatory
import packager.config.DockerSettings

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
    exec: Option[String] = Some("sh")
) {
  def toDockerSettings: DockerSettings =
    DockerSettings(
      from = from.mandatory(
        "Maintainer parameter is mandatory for docker image"
      ),
      registry = registry,
      repository = repository.mandatory(
        "Maintainer parameter is mandatory for docker image"
      ),
      tag = tag,
      exec = exec
    )
}

case object DockerOptions {

  implicit val parser = Parser[DockerOptions]
  implicit val help = Help[DockerOptions]

}
