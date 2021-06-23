package packager.config

case class RedHatSettings(
    shared: SharedSettings,
    version: String,
    description: String,
    license: String,
    release: Long,
    rpmArchitecture: String
) extends BuildSettings
