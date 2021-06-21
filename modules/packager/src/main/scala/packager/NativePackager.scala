package packager

import packager.config.BuildSettings

trait NativePackager {

  def sourceAppPath: os.Path
  def buildOptions: BuildSettings
  implicit def options = buildOptions
  def extension: String

  protected lazy val packageName: String =
    buildOptions.outputPath.last.stripSuffix(s".$extension")
  protected lazy val basePath: os.Path =
    buildOptions.workingDirectoryPath.getOrElse(
      os.temp.dir(prefix = packageName)
    )
  protected lazy val outputPath: os.Path = buildOptions.outputPath

  def build(): Unit
}
