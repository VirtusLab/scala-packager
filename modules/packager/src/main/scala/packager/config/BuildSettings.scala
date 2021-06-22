package packager.config

case class BuildSettings(
    force: Boolean = false,
    workingDirectoryPath: Option[os.Path] = None,
    outputPath: os.Path,
    version: String = "1.0.0",
    maintainer: String = "scala-packager",
    description: String = "Native package building by scala-packager",
    packageName: String = "Scala packager product",
    debian: Option[DebianSettings] = None,
    redHat: Option[RedHatSettings] = None,
    macOS: Option[MacOSSettings] = None,
    windows: Option[WindowsSettings] = None
)

case object BuildSettings {

  sealed trait PackageExtension
  case object Rpm extends PackageExtension
  case object Deb extends PackageExtension
  case object Pkg extends PackageExtension
  case object Dmg extends PackageExtension
  case object Msi extends PackageExtension

}
