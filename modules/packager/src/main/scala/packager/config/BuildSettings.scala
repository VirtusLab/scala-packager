package packager.config

import packager.config.BuildSettings.{
  DebianSettings,
  MacOsSettings,
  RedHatSettings,
  WindowsSettings
}

case class BuildSettings(
    force: Boolean = false,
    workingDirectoryPath: Option[os.Path] = None,
    outputPath: os.Path,
    version: String = "1.0.0",
    maintainer: String = "scala-packager",
    description: String = "Native package building by scala-packager",
    packageName: String = "Scala packager product",
    debian: DebianSettings = DebianSettings(),
    redHat: RedHatSettings = RedHatSettings(),
    macOS: MacOsSettings = MacOsSettings(),
    windows: WindowsSettings = WindowsSettings()
)

case object BuildSettings {

  sealed trait PackageExtension {
    def ext: String
  }
  case object RedHatExtension extends PackageExtension {
    override def ext: String = "rpm"
  }
  case object DebianExtension extends PackageExtension {
    override def ext: String = "deb"
  }
  case object PkgExtension extends PackageExtension {
    override def ext: String = "pkg"
  }
  case object DmgExtension extends PackageExtension {
    override def ext: String = "dmg"
  }
  case object WindowsExtension extends PackageExtension {
    override def ext: String = "msi"
  }

  case class RedHatSettings(
      license: String = "ASL 2.0",
      release: Long = 1,
      rpmArchitecture: String = "noarch"
  )
  case class MacOsSettings(identifier: String = "org.scala")
  case class WindowsSettings(
      licencePath: os.Path,
      productName: String = "Scala packager product"
  )
  case object WindowsSettings {

    def apply(): WindowsSettings =
      WindowsSettings(
        licencePath = defaultLicencePath
      )

    lazy val defaultLicencePath: os.Path =
      os.pwd / "modules" / "packager" / "src" / "main" / "resources" / "packager" / "common" / "apache-2.0.rtf"
  }
  case class DebianSettings(
      debianConflicts: List[String] = Nil,
      debianDependencies: List[String] = Nil,
      architecture: String = "all"
  )
}
