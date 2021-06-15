package packager

case class BuildSettings(
    force: Boolean = false,
    workingDirectoryPath: Option[os.Path] = None,
    outputPath: os.Path,
    version: String,
    maintainer: String
) {}
