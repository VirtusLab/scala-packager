package packager.config

case class DebianSettings(
    shared: SharedSettings,
    version: String,
    maintainer: String,
    description: String,
    debianConflicts: List[String],
    debianDependencies: List[String],
    architecture: String
) extends BuildSettings
