package packager.config

case class WindowsSettings(
    shared: SharedSettings,
    version: String,
    maintainer: String,
    licencePath: os.ReadablePath,
    productName: String
) extends BuildSettings
