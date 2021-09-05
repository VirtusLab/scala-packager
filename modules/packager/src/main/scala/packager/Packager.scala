package packager

import org.apache.commons.io.FilenameUtils
import packager.config.BuildSettings

trait Packager {

  def sourceAppPath: os.Path
  def buildSettings: BuildSettings

  def build(): Unit
  def launcherApp =
    FilenameUtils.removeExtension(sourceAppPath.last)

}
