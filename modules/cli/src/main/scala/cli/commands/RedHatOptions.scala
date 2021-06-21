package cli.commands

import caseapp.{Group, HelpMessage, Parser}
import caseapp.core.help.Help

final case class RedHatOptions(
    @Group("RedHat")
    @HelpMessage(
      "License that are supported by the repository, default: Apache Software License 2.0"
    )
    license: String = "ASL 2.0",
    @Group("RedHat")
    @HelpMessage(
      "The number of times this version of the software was released, default: 1"
    )
    release: Long = 1,
    @HelpMessage("Architecture that are supported by the repository, default: ")
    romArchitecture: String = "all"
)

case object RedHatOptions {

  implicit val parser = Parser[RedHatOptions]
  implicit val help = Help[RedHatOptions]

}
