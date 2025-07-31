package packager.cli.commands

import caseapp.*
import caseapp.core.help.Help

final case class SharedOptions(
  @Group("Shared")
  @HelpMessage("Set launcher app name, default: name of source app")
  @ValueDescription("launcher-app-name")
  launcherApp: Option[String] = None,
  @Group("Shared")
  @HelpMessage("The version is set to 1.0.0 by default")
  @Name("v")
  version: String = "1.0.0",
  @Group("Shared")
  @HelpMessage(
    "Set package description, default: Native package building by scala-packager"
  )
  @ValueDescription("Description")
  @Name("d")
  description: Option[String] = None,
  @Group("Shared")
  @HelpMessage(
    "It should contains names and email addresses of co-maintainers of the package"
  )
  @Name("m")
  maintainer: Option[String] = None,
  @Group("Windows")
  @HelpMessage(
    "Path to application logo in png format, it will be used to generate icon and banner/dialog in msi installer"
  )
  logoPath: Option[String] = None
)
case object SharedOptions {
  implicit lazy val parser: Parser[SharedOptions] = Parser.derive
  implicit lazy val help: Help[SharedOptions]     = Help.derive
}
