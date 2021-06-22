package packager

import packager.config.BuildSettings.PackageExtension
import packager.config.{
  BuildSettings,
  DebianSettings,
  MacOSSettings,
  RedHatSettings,
  WindowsSettings
}

trait PackageHelper {
  lazy val packageName = "echo"
  def extension: PackageExtension
  lazy val tmpDir: os.Path = TestUtils.tmpUtilDir
  lazy val echoLauncherPath: os.Path = TestUtils.echoLauncher(tmpDir)
  lazy val outputPackagePath: os.Path =
    tmpDir / s"echo.${extension.toString.toLowerCase}"
  lazy val buildOptions: BuildSettings = BuildSettings(
    force = true,
    workingDirectoryPath = Some(tmpDir),
    outputPath = outputPackagePath,
    macOS = Some(
      MacOSSettings(
        identifier = s"org.scala.$packageName"
      )
    ),
    redHat = Some(
      RedHatSettings(
        license = "ASL 2.0",
        release = 1,
        rpmArchitecture = "noarch"
      )
    ),
    windows = Some(
      WindowsSettings(
        licencePath = os.resource / "packager" / "apache-2.0",
        productName = "Scala packager product"
      )
    ),
    debian = Some(
      DebianSettings(
        debianConflicts = Nil,
        debianDependencies = Nil,
        architecture = "all"
      )
    )
  )
}
