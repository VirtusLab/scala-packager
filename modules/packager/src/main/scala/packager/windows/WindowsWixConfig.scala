package packager.windows

import packager.config.WindowsSettings
import packager.windows.wix._

case class WindowsWixConfig(
    buildSettings: WindowsSettings,
    packageName: String,
    sourcePath: os.Path,
    launcherName: String
) {

  lazy val wixExitDialog =
    buildSettings.exitDialog
      .map(txt => Property(id = WIXUI_EXITDIALOGOPTIONALTEXT, value = txt))
      .map(_.generate)
      .getOrElse("")

  lazy val wixBannerBmp = buildSettings.bannerBmp
    .map(path => WixVariable(id = WixUIBannerBmp, value = path.toString()))
    .map(_.generate)
    .getOrElse("")

  lazy val wixDialogBmp = buildSettings.dialogBmp
    .map(path => WixVariable(id = WixUIDialogBmp, value = path.toString()))
    .map(_.generate)
    .getOrElse("")

  def randomGuid: String = java.util.UUID.randomUUID.toString

  def generateContent(): String =
    s"""<?xml version="1.0"?>
    <Wix xmlns="http://schemas.microsoft.com/wix/2006/wi">
    <Product Id="*" UpgradeCode="$randomGuid"
             Name="${buildSettings.productName}" Version="${buildSettings.version}" Manufacturer="${buildSettings.maintainer}" Language="1033">
      <Package InstallerVersion="200" Compressed="yes" Comments="Windows Installer Package"/>
      <Media Id="1" Cabinet="product.cab" EmbedCab="yes"/>


      <Directory Id="TARGETDIR" Name="SourceDir">
        <Directory Id="ProgramFilesFolder">
          <Directory Id="INSTALLDIR" Name="$packageName">
            <Component Id="ApplicationFiles" Guid="$randomGuid">
              <File Id="ApplicationFile1" Source="$sourcePath" Name="$launcherName"/>
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

      <InstallExecuteSequence>
        <RemoveExistingProducts After="InstallValidate"/>
        <WriteEnvironmentStrings/>
      </InstallExecuteSequence>

      <Feature Id='Complete' Title='Foobar 1.0' Description='The complete package.'
        Display='expand' Level='1' ConfigurableDirectory='INSTALLDIR'>
        <Feature Id='MainProgram' Title='Program' Description='The main executable.' Level='1'>
        <ComponentRef Id="ApplicationFiles"/>
        <ComponentRef Id="setEnviroment"/>
        </Feature>
      </Feature>
      
      <WixVariable Id="WixUILicenseRtf" Value="${buildSettings.licencePath}" />
      <Property Id="WIXUI_INSTALLDIR" Value="INSTALLDIR" />
        
      $wixExitDialog
      $wixBannerBmp
      $wixDialogBmp

      <UIRef Id="WixUI_InstallDir" />
      
    </Product>
    </Wix>
   """

}
