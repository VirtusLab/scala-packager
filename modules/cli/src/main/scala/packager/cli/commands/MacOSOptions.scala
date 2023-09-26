package packager.cli.commands

import caseapp.{Group, HelpMessage, Parser}
import caseapp.core.help.Help
import packager.config.{MacOSSettings, SharedSettings}
import SettingsHelpers._

final case class MacOSOptions(
    @Group("MacOS")
    @HelpMessage(
      "CF Bundle Identifier"
    )
    identifier: Option[String] = None
) {
  def toMacOSSettings(sharedSettings: SharedSettings): MacOSSettings =
    MacOSSettings(
      shared = sharedSettings,
      identifier = identifier.mandatory(
        "Identifier parameter is mandatory for macOS packages"
      )
    )
}

case object MacOSOptions {

  implicit lazy val parser: Parser[MacOSOptions] = Parser.derive
  implicit lazy val help: Help[MacOSOptions] = Help.derive

}
