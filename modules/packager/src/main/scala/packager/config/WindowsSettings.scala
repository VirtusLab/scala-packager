package packager.config

case class WindowsSettings(
    licencePath: os.Path,
    productName: String
)

case object WindowsSettings {
  val default: WindowsSettings =
    WindowsSettings(
      licencePath = defaultLicencePath,
      productName = "Scala packager product"
    )

  lazy val defaultLicencePath: os.Path =
    os.pwd / "modules" / "packager" / "src" / "main" / "resources" / "packager" / "common" / "apache-2.0.rtf"
}
