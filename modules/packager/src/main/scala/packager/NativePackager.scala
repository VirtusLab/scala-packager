package packager

import packager.config.{BuildSettings, NativePackageSettings}
import packager.config.BuildSettings.PackageExtension

trait NativePackager {

  def sourceAppPath: os.Path
  def buildOptions: BuildSettings
  implicit def options = buildOptions
  def extension: PackageExtension
  def nativePackageSettings: NativePackageSettings
  lazy val defaultPackageName = buildOptions.outputPath.last
    .stripSuffix(s".${extension.toString.toLowerCase}")

  protected lazy val packageName: String =
    buildOptions.packageName.getOrElse(defaultPackageName)

  protected lazy val basePath: os.Path =
    buildOptions.workingDirectoryPath.getOrElse(
      os.temp.dir(prefix = packageName)
    )
  protected lazy val outputPath: os.Path = buildOptions.outputPath

  def build(): Unit
}
