package cli.commands

import caseapp.{Group, HelpMessage, Parser}
import caseapp.core.help.Help

final case class MacOSOptions(
    @Group("MacOS")
    @HelpMessage(
      "CF Bundle Identifier, default: org.scala.$packageName"
    )
    identifier: Option[String] = None
)

case object MacOSOptions {

  implicit val parser = Parser[MacOSOptions]
  implicit val help = Help[MacOSOptions]

}
