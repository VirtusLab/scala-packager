package cli.commands

import caseapp.{Group, HelpMessage, Parser}
import caseapp.core.help.Help
import packager.config.{RedHatSettings, SharedSettings}
import OptionsHelpers._

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
    release: Long = 1,
    @HelpMessage("Architecture that are supported by the repository, default: ")
    rpmArchitecture: String = "noarch"
) {

  def toRedHatSettings(
      sharedSettings: SharedSettings,
      sharedOptions: SharedOptions
  ): RedHatSettings =
    RedHatSettings(
      shared = sharedSettings,
      version = sharedOptions.version,
      description = sharedOptions.description,
      license = license.mandatory("License parameter is mandatory for redHat package"),
      release = release,
      rpmArchitecture = rpmArchitecture
    )
}

case object RedHatOptions {

  implicit val parser = Parser[RedHatOptions]
  implicit val help = Help[RedHatOptions]

}
