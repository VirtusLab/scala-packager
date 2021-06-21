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
    productName: String = "Scala packager product",
    debian: DebianSettings = DebianSettings(),
    redHat: RedHatSettings = RedHatSettings(),
    macOS: MacOsSettings = MacOsSettings(),
    windows: WindowsSettings = WindowsSettings()
)

case object BuildSettings {
  case class RedHatSettings(
      license: String = "ASL 2.0",
      release: Long = 1,
      rpmArchitecture: String = "noarch"
  )
  case class MacOsSettings(identifier: String = "org.scala")
  case class WindowsSettings(
      licencePath: os.Path =
        os.pwd / "modules" / "packager" / "src" / "main" / "resources" / "packager" / "common" / "apache-2.0.rtf"
  )
  case class DebianSettings(
      debianConflicts: List[String] = Nil,
      debianDependencies: List[String] = Nil,
      architecture: String = "all"
  )
}
