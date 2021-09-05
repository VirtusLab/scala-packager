package packager.config

case class DockerSettings(
    from: String,
    registry: Option[String],
    repository: String,
    tag: Option[String],
    exec: String
) extends BuildSettings
