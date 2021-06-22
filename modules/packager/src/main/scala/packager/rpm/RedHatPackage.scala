package packager.rpm

import packager.NativePackager
import packager.PackagerUtils.{osCopy, osMove, osWrite}
import packager.config.{BuildSettings, RedHatSettings}
import packager.config.BuildSettings.{PackageExtension, Rpm}

case class RedHatPackage(sourceAppPath: os.Path, buildOptions: BuildSettings)
    extends NativePackager {

  private val redHatBasePath = basePath / "rpmbuild"
  private val sourcesDirectory = redHatBasePath / "SOURCES"
  private val specsDirectory = redHatBasePath / "SPECS"
  private val rpmsDirectory = redHatBasePath / "RPMS"
  private val redHatSpec = buildRedHatSpec()

  override def nativePackageSettings: RedHatSettings =
    buildOptions.redHat.getOrElse(
      sys.error("Required settings for redHat package")
    )

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
    osMove(rpmsDirectory / s"$packageName.rpm", outputPath)

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
    osWrite(specFilePath, content)
  }

  private def buildRedHatSpec(): RedHatSpecPackage =
    RedHatSpecPackage(
      packageName = packageName,
      version = options.version,
      description = options.description,
      buildArch = nativePackageSettings.rpmArchitecture,
      license = nativePackageSettings.license,
      release = nativePackageSettings.release
    )

  private def copyExecutableFile(): Unit = {
    osCopy(sourceAppPath, sourcesDirectory / packageName)
  }

  override def extension: PackageExtension = Rpm
}
