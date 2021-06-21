package cli.commands

import caseapp.core.help.Help
import caseapp.{Group, HelpMessage, Parser}

final case class WindowsOptions(
    @Group("Windows")
    @HelpMessage("Path to license file")
    licensePath: Option[String] = None,
    @Group("Windows")
    @HelpMessage("Name of product, default: Scala packager")
    productName: String = "Scala packager"
)

case object WindowsOptions {

  implicit val parser = Parser[WindowsOptions]
  implicit val help = Help[WindowsOptions]
}
