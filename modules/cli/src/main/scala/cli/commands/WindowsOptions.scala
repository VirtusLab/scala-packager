package cli.commands

import caseapp.core.help.Help
import caseapp.{Group, HelpMessage, Parser}
import cli.commands.OptionsHelpers.Mandatory
import packager.config.WindowsSettings

final case class WindowsOptions(
    @Group("Windows")
    @HelpMessage("Path to license file")
    licensePath: Option[String] = None,
    @Group("Windows")
    @HelpMessage("Name of product, default: Scala packager")
    productName: String = "Scala packager"
) {

  def toWindowsSettings(pwd: os.Path): WindowsSettings =
    WindowsSettings(
      licencePath = os.Path(
        licensePath.mandatory(
          "License parameter is mandatory for windows packages"
        ),
        pwd
      ),
      productName = productName
    )
}

case object WindowsOptions {

  implicit val parser = Parser[WindowsOptions]
  implicit val help = Help[WindowsOptions]
}
