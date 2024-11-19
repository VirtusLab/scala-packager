package packager.cli.commands

import caseapp.core.help.Help
import caseapp.{Group, HelpMessage, Name, Parser, ValueDescription}
import SettingsHelpers.{Mandatory, Validate}
import packager.config.{SharedSettings, WindowsSettings}

final case class WindowsOptions(
  @Group("Windows")
  @HelpMessage("Path to license file")
  licensePath: Option[String] = None,
  @Group("Windows")
  @HelpMessage("Name of product, default: Scala packager")
  productName: String = "Scala packager",
  @Group("Windows")
  @HelpMessage("Text will be displayed on exit dialog")
  exitDialog: Option[String] = None,
  @Group("Windows")
  @HelpMessage(
    "Suppress Wix ICE validation (required for users that are neither interactive, not local administrators)"
  )
  suppressValidation: Boolean = false,
  @Group("Windows")
  @HelpMessage("Path to extra WIX config content")
  @ValueDescription("path")
  extraConfigs: List[String] = Nil,
  @Group("Windows")
  @HelpMessage("Whether a 64-bit executable is getting packaged")
  @Name("64")
  is64Bits: Boolean = true,
  @Group("Windows")
  @HelpMessage("WIX installer version")
  installerVersion: Option[String] = None,
  @Group("Windows")
  @HelpMessage("The GUID to identify that the windows package can be upgraded.")
  wixUpgradeCodeGuid: Option[String] = None
) {

  def toWindowsSettings(
    sharedSettings: SharedSettings,
    maintainer: Option[String]
  ): WindowsSettings =
    WindowsSettings(
      shared = sharedSettings,
      maintainer = maintainer.mandatory(
        "Maintainer parameter is mandatory for debian package"
      ),
      licencePath = os.Path(
        licensePath.mandatory(
          "License path parameter is mandatory for windows packages"
        ),
        os.pwd
      ),
      productName = productName,
      exitDialog = exitDialog,
      suppressValidation = suppressValidation,
      extraConfigs = extraConfigs,
      is64Bits = is64Bits,
      installerVersion = installerVersion,
      wixUpgradeCodeGuid = wixUpgradeCodeGuid
    )
}

case object WindowsOptions {

  implicit val parser: Parser[WindowsOptions] = Parser[WindowsOptions]
  implicit val help: Help[WindowsOptions]     = Help[WindowsOptions]
}
