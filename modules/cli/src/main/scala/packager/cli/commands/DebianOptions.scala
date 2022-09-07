package packager.cli.commands

import caseapp.core.help.Help
import caseapp.{Group, HelpMessage, Parser, ValueDescription}
import packager.cli.commands.SettingsHelpers.Mandatory
import packager.config.{DebianSettings, SharedSettings}

final case class DebianOptions(
    @Group("Debian")
    @HelpMessage(
      "The list of debian package that this package is absolute incompatibility"
    )
    @ValueDescription("debian dependencies conflicts")
    debianConflicts: List[String] = Nil,
    @Group("Debian")
    @HelpMessage("The list of debian package that this package depends on")
    @ValueDescription("debian dependencies")
    debianDependencies: List[String] = Nil,
    @Group("Debian")
    @HelpMessage(
      "Architecture that are supported by the repository, default: all"
    )
    debArchitecture: String = "all",
    @Group("Debian")
    @HelpMessage(
      "This field represents how important it is that the user have the package installed"
    )
    priority: Option[String] = None,
    @Group("Debian")
    @HelpMessage(
      "This field specifies an application area into which the package has been classified"
    )
    section: Option[String] = None
) {
  def toDebianSettings(
      sharedSettings: SharedSettings,
      maintainer: Option[String],
      description: Option[String]
  ): DebianSettings =
    DebianSettings(
      shared = sharedSettings,
      maintainer = maintainer.mandatory(
        "Maintainer parameter is mandatory for debian package"
      ),
      description = description.mandatory(
        "Description parameter is mandatory for debian package"
      ),
      debianConflicts = debianConflicts,
      debianDependencies = debianDependencies,
      architecture = debArchitecture,
      priority = priority,
      section = section
    )
}

case object DebianOptions {

  implicit val parser = Parser[DebianOptions]
  implicit val help = Help[DebianOptions]

}
