package packager.cli.commands

import caseapp.core.RemainingArgs
import caseapp.core.app.Command
import packager.cli.commands.BuildOptions.NativePackagerType.*
import packager.cli.commands.BuildOptions.PackagerType.Docker
import packager.config.SharedSettings
import packager.deb.DebianPackage
import packager.docker.DockerPackage
import packager.mac.dmg.DmgPackage
import packager.mac.pkg.PkgPackage
import packager.rpm.RedHatPackage
import packager.windows.{DefaultImageResizer, WindowsPackage}

object Build extends Command[BuildOptions] {
  override def name: String = "build"
  override def run(
    options: BuildOptions,
    remainingArgs: RemainingArgs
  ): Unit = {

    val pwd                 = os.pwd
    val destinationFileName = options.output.getOrElse(options.defaultName)

    val sourceAppPath: os.Path   = os.Path(options.sourceAppPath, pwd)
    val destinationPath: os.Path = os.Path(destinationFileName, pwd)
    val workingDirectoryPath     = options.workingDirectory.map(os.Path(_, pwd))

    val sharedSettings: SharedSettings = SharedSettings(
      sourceAppPath = sourceAppPath,
      force = options.force,
      version = options.sharedOptions.version,
      workingDirectoryPath = workingDirectoryPath,
      outputPath = destinationPath,
      launcherApp = options.sharedOptions.launcherApp,
      logoPath = options.sharedOptions.logoPath.map(os.Path(_, pwd))
    )

    def alreadyExistsCheck(): Unit =
      if (!options.force && os.exists(destinationPath)) {
        System.err.println(
          s"Error: $destinationPath already exists. Pass -f or --force to force erasing it."
        )
        sys.exit(1)
      }

    alreadyExistsCheck()

    options.packagerType match {
      case Some(Debian) =>
        DebianPackage(options.toDebianSettings(sharedSettings)).build()
      case Some(Msi) =>
        WindowsPackage(
          options.toWindowsSettings(sharedSettings),
          imageResizerOpt = Some(DefaultImageResizer)
        ).build()
      case Some(Dmg) =>
        DmgPackage(options.toMacOSSettings(sharedSettings)).build()
      case Some(Pkg) =>
        PkgPackage(options.toMacOSSettings(sharedSettings)).build()
      case Some(Rpm) =>
        RedHatPackage(options.toRedHatSettings(sharedSettings)).build()
      case Some(Docker) =>
        DockerPackage(sourceAppPath, options.toDockerSettings).build()
      case None => ()
    }
  }
}
