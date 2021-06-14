package packager.centOS

import packager.NativePackager
import packager.PackagerUtils.{osCopy, osWrite}

trait CentOSNativePackager extends NativePackager {

  protected val centOSAppPath: os.Path = basePath / s"$packageName.app"
  protected val contentPath: os.Path = centOSAppPath / "Contents"
  protected val macOsPath: os.Path = contentPath / "MacOS"
  protected val infoPlist: CentOSInfoPlist =
    CentOSInfoPlist(packageName, s"com.example.$packageName")

  def createAppDirectory(): Unit = {
    os.makeDir.all(macOsPath)

    val appPath = macOsPath / packageName
    osCopy(sourceAppPath, appPath)
  }

  protected def createInfoPlist(): Unit = {
    val infoPlistPath = contentPath / "Info.plist"

    osWrite(infoPlistPath, infoPlist.generateContent)
  }
}
