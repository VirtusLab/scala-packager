package packager.rpm

import packager.{FileUtils, NativePackager}
import packager.config.RedHatSettings

case class RedHatPackage(buildSettings: RedHatSettings) extends NativePackager {

  private val redHatBasePath = basePath / "rpmbuild"
  private val sourcesDirectory = redHatBasePath / "SOURCES"
  private val specsDirectory = redHatBasePath / "SPECS"
  private val rpmsDirectory = redHatBasePath / "RPMS"
  private val redHatSpec = buildRedHatSpec()

  override def build(): Unit = {
    createRedHatDir()

    os.proc(
        "rpmbuild",
        "-bb",
        "--build-in-place",
        "--define",
        s"_topdir $redHatBasePath",
        s"$specsDirectory/$packageName.spec"
      )
      .call(cwd = basePath)
    FileUtils.move(rpmsDirectory / s"$packageName.rpm", outputPath)

    postInstallClean()
  }

  private def postInstallClean(): Unit = {
    os.remove.all(redHatBasePath)
  }

  def createRedHatDir(): Unit = {
    os.makeDir.all(sourcesDirectory)
    os.makeDir.all(specsDirectory)

    copyExecutableFile()
    createSpecFile()
  }

  private def createSpecFile(): Unit = {
    val content = redHatSpec.generateContent
    val specFilePath = specsDirectory / s"$packageName.spec"
    FileUtils.write(specFilePath, content)
  }

  private def buildRedHatSpec(): RedHatSpecPackage =
    RedHatSpecPackage(
      launcherAppName = launcherApp,
      version = buildSettings.shared.version,
      description = buildSettings.description,
      buildArch = buildSettings.rpmArchitecture,
      license = buildSettings.license,
      release = buildSettings.release
    )

  private def copyExecutableFile(): Unit = {
    FileUtils.copy(sourceAppPath, sourcesDirectory / launcherApp)
  }

}
