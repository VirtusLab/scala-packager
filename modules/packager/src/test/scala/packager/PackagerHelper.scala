package packager

import packager.config.BuildSettings

trait PackagerHelper {
  lazy val packageName = "echo"
  lazy val tmpDir: os.Path = TestUtils.tmpUtilDir
  lazy val echoLauncherPath: os.Path = TestUtils.echoLauncher(tmpDir)
  lazy val echoNativePath: os.Path = TestUtils.echoNative(tmpDir)

  def buildSettings: BuildSettings
}
