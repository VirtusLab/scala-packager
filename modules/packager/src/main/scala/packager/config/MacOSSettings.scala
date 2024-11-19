package packager.config

case class MacOSSettings(
  shared: SharedSettings,
  identifier: String
) extends NativeSettings
