package packager.mac.dmg

import packager.config.MacOSSettings
import packager.config.BuildSettings.{Dmg, PackageExtension}
import packager.mac.MacOSNativePackager

case class DmgPackage(sourceAppPath: os.Path, buildSettings: MacOSSettings)
    extends MacOSNativePackager {

  private val tmpPackageName = s"$packageName-tmp"
  private val mountpointPath = basePath / "mountpoint"
  private val appSize: Long = os.size(sourceAppPath) / (1024L * 1024L) + 1

  override def build(): Unit = {
    os.proc(
        "hdiutil",
        "create",
        "-megabytes",
        appSize,
        "-fs",
        "HFS+",
        "-volname",
        packageName,
        tmpPackageName
      )
      .call(cwd = basePath)

    createAppDirectory()
    createInfoPlist()

    os.proc(
        "hdiutil",
        "attach",
        s"$tmpPackageName.dmg",
        "-readwrite",
        "-mountpoint",
        "mountpoint/"
      )
      .call(cwd = basePath)

    copyAppDirectory()
    removeDmgIfExists()

    os.proc("hdiutil", "detach", "mountpoint/").call(cwd = basePath)
    os.proc(
        "hdiutil",
        "convert",
        s"$tmpPackageName.dmg",
        "-format",
        "UDZO",
        "-o",
        outputPath
      )
      .call(cwd = basePath)

    postInstallClean()
  }

  private def removeDmgIfExists(): Unit = {
    if (options.shared.force && os.exists(outputPath)) os.remove(outputPath)
  }

  private def postInstallClean(): Unit = {
    os.remove(basePath / s"$tmpPackageName.dmg")
    os.remove.all(macOSAppPath)
  }

  private def copyAppDirectory(): Unit = {
    os.copy(macOSAppPath, mountpointPath / s"$packageName.app")
    os.symlink(mountpointPath / "Applications", os.root / "Applications")
  }

  override def extension: PackageExtension = Dmg
}
