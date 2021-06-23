package packager.config

case class SharedSettings(
    force: Boolean = false,
    workingDirectoryPath: Option[os.Path] = None,
    outputPath: os.Path,
    packageName: Option[String] = None
)
