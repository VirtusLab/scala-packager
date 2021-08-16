package packager.config

case class WindowsSettings(
    shared: SharedSettings,
    maintainer: String,
    licencePath: os.ReadablePath,
    productName: String,
    exitDialog: Option[String],
    suppressValidation: Boolean,
    extraConfig: Option[String]
) extends BuildSettings
