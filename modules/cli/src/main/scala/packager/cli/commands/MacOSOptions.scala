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
    identifier: Option[String] = None,
    @Group("MacOS")
    hostArchitectures: List[String] = Nil
) {
  def toMacOSSettings(sharedSettings: SharedSettings): MacOSSettings =
    MacOSSettings(
      shared = sharedSettings,
      identifier = identifier.mandatory(
        "Identifier parameter is mandatory for macOS packages"
      ),
      hostArchitectures = hostArchitectures
    )
}

case object MacOSOptions {

  implicit val parser = Parser[MacOSOptions]
  implicit val help = Help[MacOSOptions]

}
