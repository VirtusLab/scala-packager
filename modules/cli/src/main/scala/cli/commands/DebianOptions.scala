package cli.commands

import caseapp.core.help.Help
import caseapp.{Group, HelpMessage, Name, Parser, ValueDescription}

final case class DebianOptions(
    @Group("Debian")
    @HelpMessage(
      "The list of debian package that this package is absolute incompatibility"
    )
    @ValueDescription("debian dependencies conflicts")
    debianConflicts: List[String] = Nil,
    @HelpMessage("The list of debian package that this package depends on")
    @ValueDescription("debian dependencies")
    debianDependencies: List[String] = Nil,
    @HelpMessage("Architecture that are supported by the repository")
    debArchitecture: String = "all"
)

case object DebianOptions {

  implicit val parser = Parser[DebianOptions]
  implicit val help = Help[DebianOptions]

}