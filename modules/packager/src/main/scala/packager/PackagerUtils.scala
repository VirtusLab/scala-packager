package packager

import os.PermSet
import packager.config.BuildSettings

object PackagerUtils {

  def alreadyExistsCheck(
      destPath: os.Path
  )(implicit buildOptions: BuildSettings): Unit =
    if (!buildOptions.shared.force && os.exists(destPath)) {
      System.err.println(
        s"Error: $destPath already exists. Pass -f or --force to force erasing it."
      )
      sys.exit(1)
    }

  def osCopy(from: os.Path, to: os.Path)(implicit
      buildOptions: BuildSettings
  ): Unit = {
    alreadyExistsCheck(to)
    os.copy.over(from, to)
  }

  def osMove(from: os.Path, to: os.Path)(implicit
      buildOptions: BuildSettings
  ): Unit = {
    alreadyExistsCheck(to)
    os.move.over(from, to)
  }

  def osWrite(destPath: os.Path, content: String, perms: PermSet = null)(
      implicit buildOptions: BuildSettings
  ): Unit = {
    alreadyExistsCheck(destPath)
    os.write.over(destPath, content, perms)
  }

  lazy val executablePerms: PermSet = PermSet.fromString("rwxrwxr-x")

}
