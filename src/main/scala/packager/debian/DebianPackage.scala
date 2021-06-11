package packager.debian

import os.PermSet
import packager.{BuildOptions, NativePackager}

case class DebianPackage(sourceAppPath: os.Path, buildOptions: BuildOptions)
    extends NativePackager {

  private val debianBasePath = basePath / packageName
  private val usrDirectory = debianBasePath / "usr"
  private val packageInfo = buildDebianInfo()
  private val metaData = buildDebianMetaData(packageInfo)

  override def run(): Unit = {
    createConfFile
    createScriptFile
    copyExecutableFile

    os.proc("dpkg", "-b", s"./$packageName").call(cwd = basePath)
  }

  private def buildDebianMetaData(info: DebianPackageInfo): DebianMetaData =
    DebianMetaData(
      debianInfo = info
    )

  private def buildDebianInfo(): DebianPackageInfo =
    DebianPackageInfo(
      packageName = "myapp",
      version = "1.0.0",
      maintainer = "test@gmail.com",
      description = "My test package",
      homepage = "https://github.com/lwronski/projectname"
    )

  private def copyExecutableFile: Unit = {
    val scalaDirectory = usrDirectory / "share" / "scala"
    os.makeDir.all(scalaDirectory)
    os.copy(sourceAppPath, scalaDirectory / packageName)
  }

  private def createConfFile = {
    val mainDebianDirectory = debianBasePath / "DEBIAN"
    os.makeDir.all(mainDebianDirectory)
    os.write(
      mainDebianDirectory / "control",
      metaData.generateContent()
    )
  }

  private def createScriptFile: Unit = {
    val binDirectory = usrDirectory / "bin"
    os.makeDir.all(binDirectory)
    val launchScriptFile = binDirectory / packageName
    os.write(
      launchScriptFile,
      s"""#!/bin/bash
        |/usr/share/scala/$packageName
        |""".stripMargin,
      PermSet.fromString("rwxrwxr-x")
    )
  }
}
