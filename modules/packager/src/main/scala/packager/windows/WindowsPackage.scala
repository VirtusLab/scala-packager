package packager.windows

import packager.PackagerUtils.osWrite
import packager.NativePackager
import packager.config.BuildSettings
import packager.config.BuildSettings.PackageExtension.{PackageExtension, Msi}

case class WindowsPackage(sourceAppPath: os.Path, buildOptions: BuildSettings)
    extends NativePackager {

  val wixConfig: WindowsWixConfig = WindowsWixConfig(
    packageName = packageName,
    version = options.version,
    manufacturer = options.maintainer,
    productName = options.windows.productName,
    sourcePath = sourceAppPath,
    licencePath = options.windows.licencePath
  )

  private val wixConfigPath: os.Path = basePath / s"$packageName.wxs"

  override def build(): Unit = {
    createConfFile()

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

  private def createConfFile(): Unit = {
    osWrite(wixConfigPath, wixConfig.generateContent())
  }

  override def extension: PackageExtension = Msi
}
