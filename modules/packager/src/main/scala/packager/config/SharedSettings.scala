package packager.config

case class SharedSettings(
    version: String,
    force: Boolean = false,
    workingDirectoryPath: Option[os.Path] = None,
    outputPath: os.Path,
    logoPath: Option[os.Path],
    launcherAppName: Option[String] = None
)
