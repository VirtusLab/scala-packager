package cli.commands

import caseapp.core.RemainingArgs
import caseapp.core.app.Command
import BuildOptions.NativePackagerType._
import packager.config.SharedSettings
import packager.deb.DebianPackage
import packager.mac.dmg.DmgPackage
import packager.mac.pkg.PkgPackage
import packager.rpm.RedHatPackage
import packager.windows.WindowsPackage

object Build extends Command[BuildOptions] {
  override def run(
      options: BuildOptions,
      remainingArgs: RemainingArgs
  ): Unit = {

    val pwd = os.pwd
    val destinationFileName = options.output.getOrElse(options.defaultName)

    val sourceAppPath: os.Path = os.Path(options.sourceAppPath, pwd)
    val destinationPath: os.Path = os.Path(destinationFileName, pwd)
    val workingDirectoryPath = options.workingDirectory.map(os.Path(_, pwd))

    val sharedSettings: SharedSettings = SharedSettings(
      force = options.force,
      workingDirectoryPath = workingDirectoryPath,
      outputPath = destinationPath,
      packageName = options.sharedOptions.name
    )

    def alreadyExistsCheck(): Unit =
      if (!options.force && os.exists(destinationPath)) {
        System.err.println(
          s"Error: $destinationPath already exists. Pass -f or --force to force erasing it."
        )
        sys.exit(1)
      }

    alreadyExistsCheck()

    options.nativePackager match {
      case Some(Debian) =>
        DebianPackage(sourceAppPath, options.toDebianSettings(sharedSettings))
          .build()
      case Some(Msi) =>
        WindowsPackage(sourceAppPath, options.toWindowsSettings(sharedSettings))
          .build()
      case Some(Dmg) =>
        DmgPackage(sourceAppPath, options.toMacOSSettings(sharedSettings))
          .build()
      case Some(Pkg) =>
        PkgPackage(sourceAppPath, options.toMacOSSettings(sharedSettings))
          .build()
      case Some(Rpm) =>
        RedHatPackage(sourceAppPath, options.toRedHatSettings(sharedSettings))
          .build()
      case None => ()
    }
  }
}
