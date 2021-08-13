package packager.windows

import packager.PackagerUtils.osWrite
import packager.NativePackager
import packager.config.WindowsSettings
import packager.windows.WindowsUtils._
import packager.config.BuildSettings.{Msi, PackageExtension}

case class WindowsPackage(
    sourceAppPath: os.Path,
    buildSettings: WindowsSettings,
    imageResizerOpt: Option[ImageResizer] = Some(DefaultImageResizer)
) extends NativePackager {

  private val wixConfigPath: os.Path = basePath / s"$packageName.wxs"
  private val licensePath: os.Path = basePath / s"license.rtf"

  override def build(): Unit = {

    val iconPath = buildSettings.shared.logoPath.flatMap { logoPath =>
      imageResizerOpt.map(_.generateIcon(logoPath, basePath))
    }
    val bannerPath = buildSettings.shared.logoPath.flatMap { logoPath =>
      imageResizerOpt.map(_.generateBanner(logoPath, basePath))
    }
    val dialogPath = buildSettings.shared.logoPath.flatMap { logoPath =>
      imageResizerOpt.map(_.generateDialog(logoPath, basePath))
    }

    def postInstallClean() = {
      iconPath.foreach(os.remove)
      bannerPath.foreach(os.remove)
      dialogPath.foreach(os.remove)
    }

    val wixConfig = WindowsWixConfig(
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
      launcherAppName = launcherAppName,
      extraConfig = buildSettings.extraConfig
    )

    createConfFile(wixConfig)
    copyLicenseToBasePath()

    val wixBin = Option(System.getenv("WIX")).getOrElse("\"%WIX%bin\"")
    val candleBinPath = os.Path(wixBin) / "bin" / "candle.exe"
    val lightBinPath = os.Path(wixBin) / "bin" / "light.exe"

    val lightExtraArguments =
      if (buildSettings.suppressValidation) Seq("-sval")
      else Nil

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
        "WixUIExtension",
        lightExtraArguments
      )
      .call(cwd = basePath)

    postInstallClean()
  }

  private def copyLicenseToBasePath() = {
    val license =
      WindowsUtils.convertLicenseToRtfFormat(buildSettings.licencePath)
    os.write(licensePath, license)
  }

  private def createConfFile(wixConfig: WindowsWixConfig): Unit = {
    osWrite(wixConfigPath, wixConfig.generateContent())
  }

  override def extension: PackageExtension = Msi
}
