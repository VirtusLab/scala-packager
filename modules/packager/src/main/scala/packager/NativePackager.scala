package packager

import packager.config.BuildSettings
import packager.config.BuildSettings.PackageExtension

trait NativePackager {

  def sourceAppPath: os.Path
  def buildOptions: BuildSettings
  implicit def options = buildOptions
  def extension: PackageExtension

  protected lazy val packageName: String =
    buildOptions.outputPath.last.stripSuffix(s".${extension.ext}")
  protected lazy val basePath: os.Path =
    buildOptions.workingDirectoryPath.getOrElse(
      os.temp.dir(prefix = packageName)
    )
  protected lazy val outputPath: os.Path = buildOptions.outputPath

  def build(): Unit
}
