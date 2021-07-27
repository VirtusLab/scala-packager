package cli.commands

import caseapp.core.help.Help
import caseapp.{Group, HelpMessage, Parser}
import cli.commands.OptionsHelpers.Mandatory
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
    @HelpMessage("Path to bitmap, it will be used in top banner")
    bannerBmp: Option[String] = None,
    @Group("Windows")
    @HelpMessage("Background bitmap used on the welcome and completion dialogs")
    dialogBmp: Option[String] = None
) {

  def toWindowsSettings(
      sharedSettings: SharedSettings,
      sharedOptions: SharedOptions
  ): WindowsSettings =
    WindowsSettings(
      shared = sharedSettings,
      version = sharedOptions.version,
      maintainer = sharedOptions.maintainer,
      licencePath = os.Path(
        licensePath.mandatory(
          "License parameter is mandatory for windows packages"
        ),
        os.pwd
      ),
      productName = productName,
      exitDialog = exitDialog,
      bannerBmp = bannerBmp.map(os.Path(_, os.pwd)),
      dialogBmp = dialogBmp.map(os.Path(_, os.pwd))
    )
}

case object WindowsOptions {

  implicit val parser = Parser[WindowsOptions]
  implicit val help = Help[WindowsOptions]
}
