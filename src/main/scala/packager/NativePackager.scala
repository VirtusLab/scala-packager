package packager

trait NativePackager {

  def sourceAppPath: os.Path
  def packageName: String

  protected val basePath = sourceAppPath / os.RelPath("../")

  def run(): Unit
}
