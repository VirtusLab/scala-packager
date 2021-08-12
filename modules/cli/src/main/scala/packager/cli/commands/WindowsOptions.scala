package packager.cli.commands

import caseapp.core.help.Help
import caseapp.{Group, HelpMessage, Parser}
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
    @HelpMessage("Suppress Wix ICE validation (required for users that are neither interactive, not local administrators)")
    suppressValidation: Boolean = false
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
      suppressValidation = suppressValidation
    )
}

case object WindowsOptions {

  implicit val parser = Parser[WindowsOptions]
  implicit val help = Help[WindowsOptions]
}
