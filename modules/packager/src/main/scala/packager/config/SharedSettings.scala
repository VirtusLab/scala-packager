package packager.config

case class SharedSettings(
    sourceAppPath: os.Path,
    version: String,
    force: Boolean = false,
    workingDirectoryPath: Option[os.Path] = None,
    outputPath: os.Path,
    logoPath: Option[os.Path],
    launcherApp: Option[String] = None
)
