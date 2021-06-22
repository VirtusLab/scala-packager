package packager.config

case class BuildSettings(
    force: Boolean = false,
    workingDirectoryPath: Option[os.Path] = None,
    outputPath: os.Path,
    version: String = "1.0.0",
    maintainer: String = "scala-packager",
    description: String = "Native package building by scala-packager",
    packageName: String = "Scala packager product",
    debian: DebianSettings = DebianSettings.default,
    redHat: RedHatSettings = RedHatSettings.default,
    macOS: MacOsSettings = MacOsSettings.default,
    windows: WindowsSettings = WindowsSettings.default
)

case object BuildSettings {

  sealed trait PackageExtension
  case object Rpm extends PackageExtension
  case object Deb extends PackageExtension
  case object Pkg extends PackageExtension
  case object Dmg extends PackageExtension
  case object Msi extends PackageExtension

}
