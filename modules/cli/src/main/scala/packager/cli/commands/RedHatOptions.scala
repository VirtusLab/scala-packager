package packager.cli.commands

import caseapp.{Group, HelpMessage, Parser}
import caseapp.core.help.Help
import packager.config.{RedHatSettings, SharedSettings}
import SettingsHelpers._

final case class RedHatOptions(
    @Group("RedHat")
    @HelpMessage(
      "License that are supported by the repository - list of licenses https://fedoraproject.org/wiki/Licensing:Main?rd=Licensing"
    )
    license: Option[String] = None,
    @Group("RedHat")
    @HelpMessage(
      "The number of times this version of the software was released, default: 1"
    )
    release: String = "1",
    @HelpMessage(
      "Architecture that are supported by the repository, default: noarch"
    )
    rpmArchitecture: String = "noarch"
) {

  def toRedHatSettings(
      sharedSettings: SharedSettings,
      description: Option[String]
  ): RedHatSettings =
    RedHatSettings(
      shared = sharedSettings,
      description = description.mandatory(
        "Description parameter is mandatory for debian package"
      ),
      license = license.mandatory(
        "License path parameter is mandatory for redHat package"
      ),
      release = release,
      rpmArchitecture = rpmArchitecture
    )
}

case object RedHatOptions {

  implicit val parser: Parser[RedHatOptions] = Parser[RedHatOptions]
  implicit val help: Help[RedHatOptions] = Help[RedHatOptions]

}
