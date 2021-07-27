package packager.config

case class RedHatSettings(
    shared: SharedSettings,
    description: String,
    license: String,
    release: String,
    rpmArchitecture: String
) extends BuildSettings
