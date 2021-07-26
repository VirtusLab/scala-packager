package packager.config

case class SharedSettings(
    force: Boolean = false,
    workingDirectoryPath: Option[os.Path] = None,
    outputPath: os.Path,
    launcherName: Option[String] = None
)
