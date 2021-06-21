package cli.commands

import caseapp.{Group, HelpMessage, Name}

final case class WindowsOptions(
    @Group("Windows")
    @HelpMessage("Path to license file")
    licensePath: Option[String] = None,
    @Group("Windows")
    @HelpMessage("Name of product, default: scala-packager")
    productName: String = "Scala packager"
)
