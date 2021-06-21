package cli.commands

import caseapp.{Group, HelpMessage, Name, Parser, Recurse}
import caseapp.core.help.Help

final case class BuildOptions(
    @Group("Build")
    @HelpMessage("Set destination path")
    @Name("o")
    output: Option[String] = None,
    @Recurse
    sharedOptions: SharedOptions = SharedOptions(),
    @Recurse
    debian: DebianOptions = DebianOptions(),
    @Recurse
    redHat: RedHatOptions = RedHatOptions(),
    @Recurse
    macOS: MacOSOptions = MacOSOptions(),
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
    rpm: Boolean = false,
    msi: Boolean = false,
    dmg: Boolean = false,
    pkg: Boolean = false
) {

  import BuildOptions.NativePackagerType
  def nativePackager: Option[NativePackagerType] = {
    if (deb) Some(NativePackagerType.Debian)
    else if (rpm) Some(NativePackagerType.Rpm)
    else if (msi) Some(NativePackagerType.Msi)
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
    case object Msi extends NativePackagerType
    case object Dmg extends NativePackagerType
    case object Pkg extends NativePackagerType
    case object Rpm extends NativePackagerType
  }

  implicit val parser = Parser[BuildOptions]
  implicit val help = Help[BuildOptions]
}
