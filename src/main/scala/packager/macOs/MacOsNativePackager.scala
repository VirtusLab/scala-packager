package packager.macOs

import packager.NativePackager

trait MacOsNativePackager extends NativePackager {

  val macOsAppPath  = basePath / s"$packageName.app"
  val contentPath = macOsAppPath / "Contents"
  val macOsPath = contentPath / "MacOS"
  val infoPlist = MacOsInfoPlist(packageName, s"com.example.$packageName")

   def createAppDirectory() = {
    os.makeDir.all(macOsPath)
    if ( buildOptions.force) os.copy.over(sourceAppPath, macOsPath / packageName)
    else os.copy(sourceAppPath, macOsPath / packageName)
  }

  protected def createInfoPlist() = {
    os.write(contentPath / "Info.plist", infoPlist.generateContent)
  }
}
