package packager

import packager.config.BuildSettings

trait PackagerHelper {
  lazy val packageName: String           = "scalafmt"
  lazy val tmpDir: os.Path               = TestUtils.tmpUtilDir
  lazy val scalafmtNativePath: os.Path   = TestUtils.scalafmtNative(tmpDir)
  lazy val scalafmtLauncherPath: os.Path = TestUtils.scalafmtLauncher(tmpDir)
  lazy val scalafmtVersion: String       = TestUtils.scalafmtVersion

  def buildSettings: BuildSettings
}
