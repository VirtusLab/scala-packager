package packager.macOs

import packager.NativePackager
import packager.PackagerUtils.{osCopy, osWrite}

trait MacOsNativePackager extends NativePackager {

  protected val macOsAppPath: os.Path  = basePath / s"$packageName.app"
  protected val contentPath: os.Path = macOsAppPath / "Contents"
  protected val macOsPath: os.Path = contentPath / "MacOS"
  protected val infoPlist: MacOsInfoPlist = MacOsInfoPlist(packageName, s"com.example.$packageName")

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
