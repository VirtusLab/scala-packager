package packager.windows

import packager.PackagerUtils.osWrite
import packager.NativePackager
import packager.config.{BuildSettings, WindowsSettings}
import packager.config.BuildSettings.{Msi, PackageExtension}

case class WindowsPackage(sourceAppPath: os.Path, buildOptions: BuildSettings)
    extends NativePackager {

  private val wixConfigPath: os.Path = basePath / s"$packageName.wxs"
  private val licensePath: os.Path = basePath / s"license.rtf"

  private val wixConfig: WindowsWixConfig = WindowsWixConfig(
    packageName = packageName,
    version = options.version,
    manufacturer = options.maintainer,
    productName = nativePackageSettings.productName,
    sourcePath = sourceAppPath,
    licencePath = licensePath
  )

  override def nativePackageSettings: WindowsSettings =
    buildOptions.windows.getOrElse(
      sys.error("Required settings for windows package")
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
      WindowsUtils.convertLicenseToRtfFormat(nativePackageSettings.licencePath)
    os.write(licensePath, license)
  }

  private def createConfFile(): Unit = {
    osWrite(wixConfigPath, wixConfig.generateContent())
  }

  override def extension: PackageExtension = Msi
}
