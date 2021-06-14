package packager.macOs.dmg

import packager.BuildSettings
import packager.macOs.MacOsNativePackager

case class DmgPackage(sourceAppPath: os.Path, buildOptions: BuildSettings)
    extends MacOsNativePackager {

  private val tmpPackageName = s"$packageName-tmp"
  private val mountpointPath = basePath / "mountpoint"

  override def build(): Unit = {
    os.proc(
        "hdiutil",
        "create",
        "-megabytes",
        "100",
        "-fs",
        "HFS+",
        "-volname",
        tmpPackageName,
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

  private def postInstallClean(): Unit = {
    os.remove(basePath / s"$tmpPackageName.dmg")
    os.remove.all(macOsAppPath)
  }

  private def copyAppDirectory(): Unit = {
    os.copy(macOsAppPath, mountpointPath / s"$packageName.app")
    os.symlink(mountpointPath / "Applications", os.root / "Applications")
  }

  override def extension: String = "dmg"
}
