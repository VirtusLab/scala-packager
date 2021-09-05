package packager.config

case class DebianSettings(
    shared: SharedSettings,
    maintainer: String,
    description: String,
    debianConflicts: List[String],
    debianDependencies: List[String],
    architecture: String
) extends NativeSettings
