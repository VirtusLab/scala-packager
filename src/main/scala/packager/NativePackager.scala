package packager

trait NativePackager {

  def sourceAppPath: os.Path
  def buildOptions: BuildOptions

  protected val packageName: String = buildOptions.packageName
  protected val basePath: os.Path = buildOptions.basePath.getOrElse(os.temp.dir(prefix = packageName))
  protected val outputPath: os.Path = buildOptions.outputPath.getOrElse(os.pwd)

  def build(): Unit
}
