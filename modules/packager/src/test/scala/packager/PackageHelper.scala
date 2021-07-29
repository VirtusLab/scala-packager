package packager

import packager.config.BuildSettings.PackageExtension
import packager.config.{BuildSettings, SharedSettings}

trait PackageHelper {
  lazy val packageName = "echo"
  def extension: PackageExtension
  lazy val tmpDir: os.Path = TestUtils.tmpUtilDir
  lazy val echoLauncherPath: os.Path = TestUtils.echoLauncher(tmpDir)
  lazy val outputPackagePath: os.Path =
    tmpDir / s"echo.${extension.toString.toLowerCase}"
  lazy val sharedSettings: SharedSettings = SharedSettings(
    force = true,
    version = "1.0.0",
    workingDirectoryPath = Some(tmpDir),
    outputPath = outputPackagePath,
    launcherAppName = None,
    logoPath = Some(TestUtils.logo(tmpDir))
  )

  def buildSettings: BuildSettings
}
