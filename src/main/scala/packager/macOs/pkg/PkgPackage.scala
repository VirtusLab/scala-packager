package packager.macOs.pkg

import packager.BuildOptions
import packager.PackagerUtils.{executablePerms, osWrite}
import packager.macOs.MacOsNativePackager

case class PkgPackage (sourceAppPath: os.Path, buildOptions: BuildOptions)
  extends MacOsNativePackager {

  private val scriptsPath = basePath / "scripts"

  override def build(): Unit = {

    createAppDirectory()
    createInfoPlist()
    createScriptFile()

    os.proc("pkgbuild", "--install-location", "/Applications", "--component", s"$packageName.app",  outputPath / s"$packageName.pkg", "--scripts", scriptsPath)
      .call(cwd = basePath)

    postInstallClean()
  }

  private def postInstallClean(): Unit = {
    os.remove.all(macOsAppPath)
    os.remove.all(scriptsPath)
  }

  private def createScriptFile(): Unit = {
    val content = s"""#!/bin/bash
                    |rm -f /usr/local/bin/$packageName
                    |ln -s /Applications/$packageName.app/Contents/MacOS/$packageName /usr/local/bin/$packageName""".stripMargin
    os.makeDir.all(scriptsPath)
    val postInstallPath = scriptsPath / "postinstall"
    osWrite(postInstallPath, content, executablePerms, buildOptions)
  }

}


