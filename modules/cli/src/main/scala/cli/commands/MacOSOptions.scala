package cli.commands

import caseapp.{Group, HelpMessage, Parser}
import caseapp.core.help.Help
import packager.config.MacOSSettings
import OptionsHelpers._

final case class MacOSOptions(
    @Group("MacOS")
    @HelpMessage(
      "CF Bundle Identifier"
    )
    identifier: Option[String] = None
) {
  def toMacOSSettings: MacOSSettings =
    MacOSSettings(
      identifier = identifier.mandatory(
        "Identifier parameter is mandatory for macOS packages"
      )
    )
}

case object MacOSOptions {

  implicit val parser = Parser[MacOSOptions]
  implicit val help = Help[MacOSOptions]

}
