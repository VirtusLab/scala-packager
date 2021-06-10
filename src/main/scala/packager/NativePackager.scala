package packager

trait NativePackager {

  def sourceAppPath: os.Path
  def buildOptions: BuildOptions

  protected val packageName = buildOptions.packageName
  protected val basePath = sourceAppPath / os.RelPath("../")

  def run(): Unit
}
