package packager.config

import java.nio.file.Path;

case class DockerSettings(
  from: String,
  registry: Option[String],
  repository: String,
  tag: Option[String],
  exec: Option[String],
  dockerExecutable: Option[Path]
) extends BuildSettings
