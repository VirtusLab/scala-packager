package packager.windows

import packager.PackagerUtils.osWrite
import packager.NativePackager
import packager.config.WindowsSettings
import packager.windows.WindowsUtils._
import packager.config.BuildSettings.{Msi, PackageExtension}

case class WindowsPackage(
    sourceAppPath: os.Path,
    buildSettings: WindowsSettings
) extends NativePackager {

  private val wixConfigPath: os.Path = basePath / s"$packageName.wxs"
  private val licensePath: os.Path = basePath / s"license.rtf"
  private val iconPath: Option[os.Path] =
    buildSettings.shared.logoPath.map(generateIcon(_, basePath))
  private val bannerPath: Option[os.Path] =
    buildSettings.shared.logoPath.map(generateBanner(_, basePath))
  private val dialogPath: Option[os.Path] =
    buildSettings.shared.logoPath.map(generateDialog(_, basePath))

  private val wixConfig: WindowsWixConfig =
    WindowsWixConfig(
      packageName = packageName,
      sourcePath = sourceAppPath,
      iconPath = iconPath,
      bannerPath = bannerPath,
      dialogPath = dialogPath,
      licensePath = licensePath,
      exitDialog = buildSettings.exitDialog,
      productName = buildSettings.productName,
      version = buildSettings.shared.version,
      maintainer = buildSettings.maintainer,
      launcherAppName = launcherAppName
    )

  override def build(): Unit = {
    createConfFile()
    copyLicenseToBasePath()

    val wixBin = Option(System.getenv("WIX")).getOrElse("\"%WIX%bin\"")
    val candleBinPath = os.Path(wixBin) / "bin" / "candle.exe"
    val lightBinPath = os.Path(wixBin) / "bin" / "light.exe"

    os.proc(
        candleBinPath,
        wixConfigPath,
        "-ext",
        "WixUIExtension"
      )
      .call(cwd = basePath)

    os.proc(
        lightBinPath,
        s"$packageName.wixobj",
        "-o",
        outputPath,
        "-ext",
        "WixUIExtension"
      )
      .call(cwd = basePath)

    postInstallClean()
  }

  private def postInstallClean() = {
    iconPath.map(os.remove)
    bannerPath.map(os.remove)
    dialogPath.map(os.remove)
  }

  private def copyLicenseToBasePath() = {
    val license =
      WindowsUtils.convertLicenseToRtfFormat(buildSettings.licencePath)
    os.write(licensePath, license)
  }

  private def createConfFile(): Unit = {
    osWrite(wixConfigPath, wixConfig.generateContent())
  }

  override def extension: PackageExtension = Msi
}
