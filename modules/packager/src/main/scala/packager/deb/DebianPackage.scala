package packager.deb

import packager.PackagerUtils.{executablePerms, osCopy, osMove, osWrite}
import packager.{BuildSettings, NativePackager}

case class DebianPackage(sourceAppPath: os.Path, buildOptions: BuildSettings)
    extends NativePackager {

  private val debianBasePath = basePath / s"$packageName-deb"
  private val usrDirectory = debianBasePath / "usr"
  private val packageInfo = buildDebianInfo()
  private val metaData = buildDebianMetaData(packageInfo)
  private val mainDebianDirectory = debianBasePath / "DEBIAN"

  override def build(): Unit = {
    createDebianDir()

    os.proc("dpkg", "-b", debianBasePath).call(cwd = basePath)
    osMove(basePath / s"$packageName-deb.deb", outputPath)

    postInstallClean()
  }

  private def postInstallClean(): Unit = {
    os.remove.all(debianBasePath)
  }

  def createDebianDir(): Unit = {
    os.makeDir.all(mainDebianDirectory)

    createConfFile()
    createScriptFile()
    copyExecutableFile()
  }

  private def buildDebianMetaData(info: DebianPackageInfo): DebianMetaData =
    DebianMetaData(
      debianInfo = info
    )

  private def buildDebianInfo(): DebianPackageInfo =
    DebianPackageInfo(
      packageName = packageName,
      version = buildOptions.version,
      maintainer = buildOptions.maintainer,
      description = buildOptions.description
    )

  private def copyExecutableFile(): Unit = {
    val scalaDirectory = usrDirectory / "share" / "scala"
    os.makeDir.all(scalaDirectory)
    osCopy(sourceAppPath, scalaDirectory / packageName)
  }

  private def createConfFile(): Unit = {
    osWrite(mainDebianDirectory / "control", metaData.generateContent())
  }

  private def createScriptFile(): Unit = {
    val binDirectory = usrDirectory / "bin"
    os.makeDir.all(binDirectory)
    val launchScriptFile = binDirectory / packageName
    val content = s"""#!/bin/bash
                      |/usr/share/scala/$packageName \"$$@\"
                      |""".stripMargin
    osWrite(launchScriptFile, content, executablePerms)
  }

  override def extension: String = "deb"
}
