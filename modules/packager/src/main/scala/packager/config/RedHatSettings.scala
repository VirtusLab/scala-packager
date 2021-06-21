package packager.config

case class RedHatSettings(
    license: String,
    release: Long,
    rpmArchitecture: String
)

case object RedHatSettings {
  val default: RedHatSettings = RedHatSettings(
    license = "ASL 2.0",
    release = 1,
    rpmArchitecture = "noarch"
  )
}
