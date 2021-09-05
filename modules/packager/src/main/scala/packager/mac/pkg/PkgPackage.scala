package packager.mac.pkg

import packager.FileUtils
import packager.config.MacOSSettings
import packager.mac.MacOSNativePackager

case class PkgPackage(buildSettings: MacOSSettings)
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
                    |rm -f /usr/local/bin/$launcherApp
                    |ln -s /Applications/$packageName.app/Contents/MacOS/$launcherApp /usr/local/bin/$launcherApp""".stripMargin
    os.makeDir.all(scriptsPath)
    val postInstallPath = scriptsPath / "postinstall"
    FileUtils.write(postInstallPath, content, FileUtils.executablePerms)
  }

}
