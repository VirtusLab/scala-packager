package packager.cli.commands

import caseapp._
import packager.cli.commands.BuildOptions.PackagerType
import packager.config._

final case class BuildOptions(
    @Group("Packager")
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
    @Recurse
    windows: WindowsOptions = WindowsOptions(),
    @Recurse
    dockerOptions: DockerOptions = DockerOptions(),
    @Group("Packager")
    @HelpMessage("Overwrite destination file if it exists")
    @Name("f")
    force: Boolean = false,
    @Group("Packager")
    @HelpMessage("Set working directory path")
    @Name("w")
    workingDirectory: Option[String] = None,
    @Group("Packager")
    @HelpMessage("Source app path")
    @Name("a")
    sourceAppPath: String,
    @Group("Packager")
    @HelpMessage("Build debian package, available only on linux")
    deb: Boolean = false,
    @Group("Packager")
    @HelpMessage("Build rpm package, available only on linux")
    rpm: Boolean = false,
    @Group("Packager")
    @HelpMessage("Build msi package, available only on windows")
    msi: Boolean = false,
    @Group("Packager")
    @HelpMessage("Build dmg package, available only on centOS")
    dmg: Boolean = false,
    @Group("Packager")
    @HelpMessage("Build pkg package, available only on centOS")
    pkg: Boolean = false,
    @Group("Packager")
    @HelpMessage("Build docker image")
    docker: Boolean = false
) {

  import BuildOptions.NativePackagerType
  def packagerType: Option[PackagerType] = {
    if (deb) Some(NativePackagerType.Debian)
    else if (rpm) Some(NativePackagerType.Rpm)
    else if (msi) Some(NativePackagerType.Msi)
    else if (dmg) Some(NativePackagerType.Dmg)
    else if (pkg) Some(NativePackagerType.Pkg)
    else if (docker) Some(PackagerType.Docker)
    else None
  }

  def defaultName: String = {
    if (deb) "app.deb"
    else if (rpm) "app.rpm"
    else if (msi) "app.msi"
    else if (dmg) "app.dmg"
    else if (pkg) "app.pkg"
    else if (msi) "app.msi"
    else "app"
  }

  def toDebianSettings(sharedSettings: SharedSettings): DebianSettings =
    debian.toDebianSettings(
      sharedSettings,
      sharedOptions.maintainer,
      sharedOptions.description
    )

  def toWindowsSettings(sharedSettings: SharedSettings): WindowsSettings =
    windows.toWindowsSettings(sharedSettings, sharedOptions.maintainer)

  def toMacOSSettings(sharedSettings: SharedSettings): MacOSSettings =
    macOS.toMacOSSettings(sharedSettings)

  def toRedHatSettings(sharedSettings: SharedSettings): RedHatSettings =
    redHat.toRedHatSettings(sharedSettings, sharedOptions.description)

  def toDockerSettings: DockerSettings = dockerOptions.toDockerSettings
}

object BuildOptions {

  sealed abstract class PackagerType extends Product with Serializable
  case object PackagerType {
    case object Docker extends PackagerType
  }
  sealed abstract class NativePackagerType extends PackagerType
  case object NativePackagerType {
    case object Debian extends NativePackagerType
    case object Msi extends NativePackagerType
    case object Dmg extends NativePackagerType
    case object Pkg extends NativePackagerType
    case object Rpm extends NativePackagerType
  }

  implicit lazy val parser: Parser[BuildOptions] = Parser.derive
  implicit lazy val help: Help[BuildOptions] = Help.derive
}
