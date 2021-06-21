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

  object PackageExtension extends Enumeration {
    type PackageExtension = Value
    val Rpm, Deb, Pkg, Dmg, Msi = Value
  }
//  sealed trait PackageExtension {
//    def ext: String
//  }
//  case object RedHatExtension extends PackageExtension {
//    override def ext: String = "rpm"
//  }
//  case object DebianExtension extends PackageExtension {
//    override def ext: String = "deb"
//  }
//  case object PkgExtension extends PackageExtension {
//    override def ext: String = "pkg"
//  }
//  case object DmgExtension extends PackageExtension {
//    override def ext: String = "dmg"
//  }
//  case object WindowsExtension extends PackageExtension {
//    override def ext: String = "msi"
//  }
}
