package packager.config

case class WindowsSettings(
    licencePath: os.ReadablePath,
    productName: String
)

case object WindowsSettings {
  val default: WindowsSettings =
    WindowsSettings(
      licencePath = defaultLicencePath,
      productName = "Scala packager product"
    )

  lazy val defaultLicencePath: os.ReadablePath =
    os.resource / "packager" / "common" / "apache-2.0"
}
