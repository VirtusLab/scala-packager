package packager

case class BuildOptions(packageName: String, force: Boolean = false, basePath: Option[os.Path] = None, outputPath: Option[os.Path] = None) {

}
