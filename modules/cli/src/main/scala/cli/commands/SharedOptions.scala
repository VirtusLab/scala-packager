package cli.commands

import caseapp.{Group, HelpMessage, Name, Parser, ValueDescription}
import caseapp.core.help.Help

final case class SharedOptions(
    @Group("Shared")
    @HelpMessage("Set package name, default: name of source app")
    @ValueDescription("name")
    name: Option[String] = None,
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
    description: String = "Native package building by scala-packager",
    @Group("Shared")
    @HelpMessage(
      "The maintainer is set to scala-packager by default, it should contains names and email addresses of co-maintainers of the package"
    )
    @Name("m")
    maintainer: String = "scala-packager"
)
case object SharedOptions {

  implicit val parser = Parser[SharedOptions]
  implicit val help = Help[SharedOptions]
}
