package packager.config

case class WindowsSettings(
  shared: SharedSettings,
  maintainer: String,
  licencePath: os.ReadablePath,
  productName: String,
  exitDialog: Option[String],
  suppressValidation: Boolean,
  extraConfigs: List[String],
  is64Bits: Boolean,
  installerVersion: Option[String],
  wixUpgradeCodeGuid: Option[String]
) extends NativeSettings
