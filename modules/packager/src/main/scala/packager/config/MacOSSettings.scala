package packager.config

case class MacOSSettings(
    shared: SharedSettings,
    identifier: String,
    hostArchitectures: List[String]
) extends NativeSettings
