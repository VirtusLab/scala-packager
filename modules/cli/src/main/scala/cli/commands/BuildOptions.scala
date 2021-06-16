package cli.commands

import caseapp.{Group, HelpMessage, Name, Parser}
import caseapp.core.help.Help

final case class BuildOptions(
    @Group("Build")
    @HelpMessage("Set destination path")
    @Name("o")
    output: Option[String] = None,
    @Group("Build")
    @HelpMessage("Overwrite destination file if it exists")
    @Name("f")
    force: Boolean = false,
    @Group("Build")
    @HelpMessage("Set working directory path")
    @Name("w")
    workingDirectory: Option[String] = None,
    @Group("Build")
    @HelpMessage("Source app path")
    @Name("a")
    sourceAppPath: String,
    deb: Boolean = false,
    msi: Boolean = false,
    dmg: Boolean = false,
    pkg: Boolean = false,
    @Group("Build")
    @HelpMessage("The version is set to 1.0.0 by default")
    @Name("v")
    version: String = "1.0.0",
    @Group("Build")
    @HelpMessage(
      "The maintainer is set to scala-packager by default, it should contains names and email addresses of co-maintainers of the package"
    )
    @Name("v")
    maintainer: String = "scala-packager",
    @Name("d")
    description: String = "Native package building by scala-packager"
) {

  import BuildOptions.NativePackagerType
  def nativePackager: Option[NativePackagerType] = {
    if (deb) Some(NativePackagerType.Debian)
    else if (msi) Some(NativePackagerType.Windows)
    else if (dmg) Some(NativePackagerType.Dmg)
    else if (pkg) Some(NativePackagerType.Pkg)
    else None
  }

  def defaultName: String = {
    if (deb) "app.deb"
    else if (msi) "app.msi"
    else if (dmg) "app.dmg"
    else if (pkg) "app.pkg"
    else "package"
  }

}

object BuildOptions {

  sealed abstract class NativePackagerType extends Product with Serializable
  case object NativePackagerType {
    case object Debian extends NativePackagerType
    case object Windows extends NativePackagerType
    case object Dmg extends NativePackagerType
    case object Pkg extends NativePackagerType
  }

  implicit val parser = Parser[BuildOptions]
  implicit val help = Help[BuildOptions]
}
