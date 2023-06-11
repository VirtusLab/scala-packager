package packager

import packager.config.NativeSettings

import org.apache.commons.io.FilenameUtils

trait NativePackager extends Packager {

  implicit def options: NativeSettings = buildSettings
  override def buildSettings: NativeSettings

  lazy val sourceAppPath: os.Path = buildSettings.shared.sourceAppPath

  override def launcherApp: String =
    buildSettings.shared.launcherApp.getOrElse(super.launcherApp).toLowerCase()

  lazy val outputPath: os.Path = buildSettings.shared.outputPath

  lazy val packageName =
    FilenameUtils.removeExtension(buildSettings.shared.outputPath.last)

  lazy val basePath: os.Path =
    buildSettings.shared.workingDirectoryPath.getOrElse(
      os.temp.dir(prefix = packageName)
    )

}
