package packager.windows

import packager.{FileUtils, NativePackager}
import packager.config.WindowsSettings

case class WindowsPackage(
  buildSettings: WindowsSettings,
  imageResizerOpt: Option[ImageResizer] = None
) extends NativePackager {

  private val wixConfigPath: os.Path = basePath / s"$packageName.wxs"
  private val licensePath: os.Path   = basePath / s"license.rtf"

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
      launcherAppName = launcherApp,
      extraConfigs = buildSettings.extraConfigs,
      is64Bits = buildSettings.is64Bits,
      installerVersion = buildSettings.installerVersion,
      wixUpgradeCodeGuid = buildSettings.wixUpgradeCodeGuid
    )

    createConfFile(wixConfig)
    copyLicenseToBasePath()

    val wixBin        = Option(System.getenv("WIX")).getOrElse("\"%WIX%bin\"")
    val candleBinPath = os.Path(wixBin) / "bin" / "candle.exe"
    val lightBinPath  = os.Path(wixBin) / "bin" / "light.exe"

    val lightExtraArguments =
      if (buildSettings.suppressValidation) Seq("-sval")
      else Nil

    val extraCandleOptions =
      if (buildSettings.is64Bits) Seq("-arch", "x64")
      else Nil

    os.proc(
      candleBinPath,
      wixConfigPath,
      extraCandleOptions,
      "-ext",
      "WixUIExtension"
    ).call(cwd = basePath)

    os.proc(
      lightBinPath,
      s"$packageName.wixobj",
      "-o",
      outputPath,
      "-ext",
      "WixUIExtension",
      lightExtraArguments
    ).call(cwd = basePath)

    postInstallClean()
  }

  private def copyLicenseToBasePath() = {
    val license =
      WindowsUtils.convertLicenseToRtfFormat(buildSettings.licencePath)
    os.write.over(licensePath, license)
  }

  private def createConfFile(wixConfig: WindowsWixConfig): Unit = {
    FileUtils.write(wixConfigPath, wixConfig.generateContent())
  }

}
