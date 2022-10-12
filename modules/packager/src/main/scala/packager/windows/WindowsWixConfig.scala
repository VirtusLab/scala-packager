package packager.windows

import packager.windows.wix._
import java.nio.charset.Charset

import scala.io.Codec

case class WindowsWixConfig(
    packageName: String,
    sourcePath: os.Path,
    iconPath: Option[os.Path],
    bannerPath: Option[os.Path],
    dialogPath: Option[os.Path],
    licensePath: os.Path,
    exitDialog: Option[String],
    productName: String,
    version: String,
    maintainer: String,
    launcherAppName: String,
    extraConfigs: List[String],
    is64Bits: Boolean,
    installerVersion: Option[String],
    wixUpgradeCodeGuid: Option[String]
) {

  lazy val extraConfig: Option[String] =
    if (extraConfigs.isEmpty) None
    else
      Some {
        extraConfigs
          .map { path =>
            val path0 = os.Path(path, os.pwd)
            os.read(path0, Codec(Charset.defaultCharset()))
          }
          .mkString(System.lineSeparator())
      }

  lazy val wixExitDialog =
    exitDialog
      .map(txt => Property(id = WIXUI_EXITDIALOGOPTIONALTEXT, value = txt))
      .map(_.generate)
      .getOrElse("")

  lazy val wixBannerBmp = bannerPath
    .map(path => WixVariable(id = WixUIBannerBmp, value = path.toString()))
    .map(_.generate)
    .getOrElse("")

  lazy val wixDialogBmp = dialogPath
    .map(path => WixVariable(id = WixUIDialogBmp, value = path.toString()))
    .map(_.generate)
    .getOrElse("")

  lazy val wixPropertyIcon = iconPath
    .map(_ => Property(id = ARPPRODUCTICON, value = "logo_ico"))
    .map(_.generate)
    .getOrElse("")

  lazy val wixIcon = iconPath
    .map(path => Icon(id = "logo_ico", sourceFile = path.toString()))
    .map(_.generate)
    .getOrElse("")

  def randomGuid: String = java.util.UUID.randomUUID.toString

  private def extraPackage =
    if (is64Bits) """Platform="x64""""
    else ""
  private def programFiles =
    if (is64Bits) "ProgramFiles64Folder"
    else "ProgramFilesFolder"

  def generateContent(): String =
    s"""<?xml version="1.0"?>
    <Wix xmlns="http://schemas.microsoft.com/wix/2006/wi">
    <Product Id="*" UpgradeCode="${wixUpgradeCodeGuid.getOrElse(randomGuid)}"
             Name="$productName" Version="$version" Manufacturer="$maintainer" Language="1033">
      <Package $extraPackage InstallerVersion="${installerVersion.getOrElse(
      "200"
    )}" Compressed="yes" Comments="Windows Installer Package"/>
      <Media Id="1" Cabinet="product.cab" EmbedCab="yes"/>


      <Directory Id="TARGETDIR" Name="SourceDir">
        <Directory Id="$programFiles">
          <Directory Id="INSTALLDIR" Name="$packageName">
            <Component Id="ApplicationFiles" Guid="$randomGuid">
              <File Id="ApplicationFile1" Source="$sourcePath" Name="$launcherAppName.${sourcePath.ext}"/>
            </Component>
          </Directory>
        </Directory>
      </Directory>

      <DirectoryRef Id="TARGETDIR">
        <Component Id ="setEnviroment"
                   Guid="$randomGuid">
          <CreateFolder />
          <Environment Id="SET_ENV"
                       Action="set"
                       Name="PATH"
                       Part="last"
                       Permanent="no"
                       System="yes"
                       Value="[INSTALLDIR]" />
        </Component>
      </DirectoryRef>

      <MajorUpgrade Schedule="afterInstallInitialize"
        DowngradeErrorMessage="A later version of $productName is already installed. Installer will now exit."
        AllowDowngrades="no"
      />

      <Feature Id='Complete' Title='Foobar 1.0' Description='The complete package.'
        Display='expand' Level='1' ConfigurableDirectory='INSTALLDIR'>
        <Feature Id='MainProgram' Title='Program' Description='The main executable.' Level='1'>
        <ComponentRef Id="ApplicationFiles"/>
        <ComponentRef Id="setEnviroment"/>
        </Feature>
      </Feature>

      <WixVariable Id="WixUILicenseRtf" Value="$licensePath" />
      <Property Id="WIXUI_INSTALLDIR" Value="INSTALLDIR" />

      $wixExitDialog
      $wixBannerBmp
      $wixDialogBmp
      $wixPropertyIcon
      $wixIcon

      <UIRef Id="WixUI_InstallDir" />

      ${extraConfig.getOrElse("")}

    </Product>
    </Wix>
   """

}
