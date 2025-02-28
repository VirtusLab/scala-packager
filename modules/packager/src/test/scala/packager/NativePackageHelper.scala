package packager

import packager.config.{NativeSettings, SharedSettings}

trait NativePackageHelper extends PackagerHelper {

  def buildSettings: NativeSettings
  def outputPackagePath: os.Path

  lazy val sharedSettings: SharedSettings =
    SharedSettings(
      sourceAppPath = scalafmtLauncherPath,
      force = true,
      version = "1.0.0",
      workingDirectoryPath = Some(tmpDir),
      outputPath = outputPackagePath,
      launcherApp = None,
      logoPath = Some(TestUtils.logo(tmpDir))
    )

}
