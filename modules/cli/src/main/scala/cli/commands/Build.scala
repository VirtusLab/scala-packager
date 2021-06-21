package cli.commands

import caseapp.core.RemainingArgs
import caseapp.core.app.Command
import BuildOptions.NativePackagerType._
import packager.config.{
  BuildSettings,
  DebianSettings,
  MacOsSettings,
  RedHatSettings,
  WindowsSettings
}
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

    val buildSettings: BuildSettings = BuildSettings(
      force = options.force,
      workingDirectoryPath = workingDirectoryPath,
      outputPath = destinationPath,
      version = options.sharedOptions.version,
      maintainer = options.sharedOptions.maintainer,
      description = options.sharedOptions.description,
      packageName = options.sharedOptions.name
        .orElse(sourceAppPath.last.split('.').headOption)
        .getOrElse("Scala Packager"),
      debian = DebianSettings(
        debianConflicts = options.debian.debianConflicts,
        debianDependencies = options.debian.debianDependencies,
        architecture = options.debian.debArchitecture
      ),
      redHat = RedHatSettings(
        license = options.redHat.license,
        release = options.redHat.release,
        rpmArchitecture = options.redHat.romArchitecture
      ),
      macOS = MacOsSettings(
        identifier = options.macOS.identifier
          .getOrElse(s"org.scala.${options.sharedOptions.name}")
      ),
      windows = WindowsSettings(
        licencePath = options.windows.licensePath
          .map(os.Path(_, pwd))
          .getOrElse(WindowsSettings.defaultLicencePath),
        productName = options.windows.productName
      )
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
      case Some(Msi) =>
        WindowsPackage(sourceAppPath, buildSettings).build()
      case Some(Dmg) =>
        DmgPackage(sourceAppPath, buildSettings).build()
      case Some(Pkg) =>
        PkgPackage(sourceAppPath, buildSettings).build()
      case Some(Rpm) =>
        RedHatPackage(sourceAppPath, buildSettings).build()
      case None => ()
    }
  }
}
