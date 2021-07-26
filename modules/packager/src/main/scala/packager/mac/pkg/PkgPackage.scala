package packager.mac.pkg

import packager.PackagerUtils.{executablePerms, osWrite}
import packager.config.MacOSSettings
import packager.config.BuildSettings.{PackageExtension, Pkg}
import packager.mac.MacOSNativePackager

case class PkgPackage(sourceAppPath: os.Path, buildSettings: MacOSSettings)
    extends MacOSNativePackager {

  private val scriptsPath = basePath / "scripts"

  override def build(): Unit = {

    createAppDirectory()
    createInfoPlist()
    createScriptFile()

    os.proc(
        "pkgbuild",
        "--install-location",
        "/Applications",
        "--component",
        s"$packageName.app",
        outputPath,
        "--scripts",
        scriptsPath
      )
      .call(cwd = basePath)

    postInstallClean()
  }

  private def postInstallClean(): Unit = {
    os.remove.all(macOSAppPath)
    os.remove.all(scriptsPath)
  }

  private def createScriptFile(): Unit = {
    val content = s"""#!/bin/bash
                    |rm -f /usr/local/bin/$launcherName
                    |ln -s /Applications/$packageName.app/Contents/MacOS/$launcherName /usr/local/bin/$launcherName""".stripMargin
    os.makeDir.all(scriptsPath)
    val postInstallPath = scriptsPath / "postinstall"
    osWrite(postInstallPath, content, executablePerms)
  }

  override def extension: PackageExtension = Pkg
}
