package packager.config

case class DebianSettings(
    debianConflicts: List[String],
    debianDependencies: List[String],
    architecture: String
) extends NativePackageSettings
