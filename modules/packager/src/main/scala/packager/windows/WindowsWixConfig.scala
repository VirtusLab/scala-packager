package packager.windows

case class WindowsWixConfig(
    packageName: String,
    version: String,
    manufacturer: String,
    productName: String,
    sourcePath: os.Path,
    licencePath: os.ReadablePath
) {

  def randomGuid: String = java.util.UUID.randomUUID.toString

  def generateContent(): String =
    s"""<?xml version="1.0"?>
    <Wix xmlns="http://schemas.microsoft.com/wix/2006/wi">
    <Product Id="*" UpgradeCode="$randomGuid"
             Name="$productName" Version="$version" Manufacturer="$manufacturer" Language="1033">
      <Package InstallerVersion="200" Compressed="yes" Comments="Windows Installer Package"/>
      <Media Id="1" Cabinet="product.cab" EmbedCab="yes"/>


      <Directory Id="TARGETDIR" Name="SourceDir">
        <Directory Id="ProgramFilesFolder">
          <Directory Id="INSTALLDIR" Name="$packageName">
            <Component Id="ApplicationFiles" Guid="$randomGuid">
              <File Id="ApplicationFile1" Source="$sourcePath" Name="${sourcePath.last}"/>
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
      
      <WixVariable Id="WixUILicenseRtf" Value="${licencePath}" />
      <Property Id="WIXUI_INSTALLDIR" Value="INSTALLDIR" />

      <UIRef Id="WixUI_InstallDir" />
      
    </Product>
    </Wix>
   """

}
