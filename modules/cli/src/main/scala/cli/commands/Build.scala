package cli.commands

import caseapp.core.RemainingArgs
import caseapp.core.app.Command
import BuildOptions.NativePackagerType._
import packager.BuildSettings
import packager.deb.DebianPackage
import packager.mac.dmg.DmgPackage
import packager.mac.pkg.PkgPackage

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

    val buildSettings: BuildSettings = BuildSettings(
      force = options.force,
      workingDirectoryPath = workingDirectoryPath,
      outputPath = destinationPath,
      version = options.version,
      maintainer = options.maintainer,
      description = options.description
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
        DebianPackage(sourceAppPath, buildSettings).build()
      case Some(Windows) => ???
      case Some(Dmg) =>
        DmgPackage(sourceAppPath, buildSettings).build()
      case Some(Pkg) =>
        PkgPackage(sourceAppPath, buildSettings).build()
      case None => ()
    }
  }
}
