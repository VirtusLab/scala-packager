package packager.config

case class RedHatSettings(
    license: String,
    release: Long,
    rpmArchitecture: String
) extends NativePackageSettings
