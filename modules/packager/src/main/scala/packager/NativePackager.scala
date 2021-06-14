package packager

trait NativePackager {

  def sourceAppPath: os.Path
  def buildOptions: BuildSettings
  implicit val options = buildOptions
  def extension: String

  protected val packageName: String =
    buildOptions.outputPath.last.stripSuffix(s".$extension")
  protected val basePath: os.Path =
    buildOptions.workingDirectoryPath.getOrElse(
      os.temp.dir(prefix = packageName)
    )
  protected val outputPath: os.Path = buildOptions.outputPath

  def build(): Unit
}
