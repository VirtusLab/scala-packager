package packager

case class BuildOptions(
                         packageName: String,
                         force: Boolean = false,
                         workingDirPath: Option[os.Path] = None,
                         outputPath: Option[os.Path] = None) {
}
