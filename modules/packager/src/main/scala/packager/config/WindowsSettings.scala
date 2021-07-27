package packager.config

case class WindowsSettings(
    shared: SharedSettings,
    version: String,
    maintainer: String,
    licencePath: os.ReadablePath,
    productName: String,
    exitDialog: Option[String],
    bannerBmp: Option[os.Path],
    dialogBmp: Option[os.Path]
) extends BuildSettings
