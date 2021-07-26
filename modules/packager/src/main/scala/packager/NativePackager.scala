package packager

import packager.config.BuildSettings
import packager.config.BuildSettings.PackageExtension

trait NativePackager {

  def sourceAppPath: os.Path
  def buildSettings: BuildSettings
  implicit def options = buildSettings
  def extension: PackageExtension

  lazy val packageName = buildSettings.shared.outputPath.last
    .stripSuffix(s".${extension.toString.toLowerCase}")

  protected lazy val launcherName: String =
    buildSettings.shared.launcherName.getOrElse(sourceAppPath.last)

  protected lazy val basePath: os.Path =
    buildSettings.shared.workingDirectoryPath.getOrElse(
      os.temp.dir(prefix = packageName)
    )
  protected lazy val outputPath: os.Path = buildSettings.shared.outputPath

  def build(): Unit
}
