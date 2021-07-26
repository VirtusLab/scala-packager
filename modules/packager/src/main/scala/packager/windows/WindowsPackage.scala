package packager.windows

import packager.PackagerUtils.osWrite
import packager.NativePackager
import packager.config.WindowsSettings
import packager.config.BuildSettings.{Msi, PackageExtension}

case class WindowsPackage(
    sourceAppPath: os.Path,
    buildSettings: WindowsSettings
) extends NativePackager {

  private val wixConfigPath: os.Path = basePath / s"$packageName.wxs"
  private val licensePath: os.Path = basePath / s"license.rtf"

  private val wixConfig: WindowsWixConfig = WindowsWixConfig(
    packageName = packageName,
    version = buildSettings.version,
    manufacturer = buildSettings.maintainer,
    productName = buildSettings.productName,
    sourcePath = sourceAppPath,
    launcherName = launcherName,
    licencePath = licensePath
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
