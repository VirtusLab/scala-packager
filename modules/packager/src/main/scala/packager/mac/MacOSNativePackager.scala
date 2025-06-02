package packager.mac

import packager.{FileUtils, NativePackager}
import packager.config.MacOSSettings

trait MacOSNativePackager extends NativePackager {

  protected val macOSAppPath: os.Path     = basePath / s"$packageName.app"
  protected val contentPath: os.Path      = macOSAppPath / "Contents"
  protected val macOsPath: os.Path        = contentPath / "MacOS"
  protected val infoPlist: MacOSInfoPlist =
    MacOSInfoPlist(packageName, buildSettings.identifier)

  override def buildSettings: MacOSSettings

  def createAppDirectory(): Unit = {
    os.makeDir.all(macOsPath)

    val appPath = macOsPath / launcherApp
    FileUtils.copy(sourceAppPath, appPath)
  }

  protected def createInfoPlist(): Unit = {
    val infoPlistPath = contentPath / "Info.plist"

    FileUtils.write(infoPlistPath, infoPlist.generateContent)
  }
}
