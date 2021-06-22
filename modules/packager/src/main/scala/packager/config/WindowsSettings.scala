package packager.config

case class WindowsSettings(
    licencePath: os.ReadablePath,
    productName: String
) extends NativePackageSettings
