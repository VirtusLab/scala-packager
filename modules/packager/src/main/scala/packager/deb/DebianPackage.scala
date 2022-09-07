package packager.deb

import packager.{FileUtils, NativePackager}
import packager.config.DebianSettings

case class DebianPackage(buildSettings: DebianSettings) extends NativePackager {

  private val debianBasePath = basePath / "debian"
  private val usrDirectory = debianBasePath / "usr"
  private val packageInfo = buildDebianInfo()
  private val metaData = buildDebianMetaData(packageInfo)
  private val mainDebianDirectory = debianBasePath / "DEBIAN"

  override def build(): Unit = {
    createDebianDir()

    os.proc("dpkg", "-b", debianBasePath)
      .call(cwd = basePath)

    FileUtils.move(basePath / "debian.deb", outputPath)

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
      debianInfo = info,
      architecture = buildSettings.architecture,
      dependsOn = buildSettings.debianDependencies,
      conflicts = buildSettings.debianConflicts,
      priority = buildSettings.priority,
      section = buildSettings.section
    )

  private def buildDebianInfo(): DebianPackageInfo =
    DebianPackageInfo(
      packageName = packageName,
      version = buildSettings.shared.version,
      maintainer = buildSettings.maintainer,
      description = buildSettings.description
    )

  private def copyExecutableFile(): Unit = {
    val scalaDirectory = usrDirectory / "share" / "scala"
    os.makeDir.all(scalaDirectory)
    FileUtils.copy(sourceAppPath, scalaDirectory / launcherApp)
  }

  private def createConfFile(): Unit = {
    FileUtils.write(mainDebianDirectory / "control", metaData.generateContent())
  }

  private def createScriptFile(): Unit = {
    val binDirectory = usrDirectory / "bin"
    os.makeDir.all(binDirectory)
    val launchScriptFile = binDirectory / launcherApp
    val content = s"""#!/bin/bash
                      |/usr/share/scala/$launcherApp \"$$@\"
                      |""".stripMargin
    FileUtils.write(launchScriptFile, content, FileUtils.executablePerms)
  }

}
