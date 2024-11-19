package packager

import os.PermSet
import packager.config.{BuildSettings, NativeSettings}

object FileUtils {

  def alreadyExistsCheck(
    destPath: os.Path
  )(implicit buildOptions: NativeSettings): Unit =
    if (!buildOptions.shared.force && os.exists(destPath)) {
      System.err.println(
        s"Error: $destPath already exists. Pass -f or --force to force erasing it."
      )
      sys.exit(1)
    }

  def copy(from: os.Path, to: os.Path)(implicit
    buildOptions: NativeSettings
  ): Unit = {
    alreadyExistsCheck(to)
    os.copy.over(from, to)
  }

  def move(from: os.Path, to: os.Path)(implicit
    buildOptions: NativeSettings
  ): Unit = {
    alreadyExistsCheck(to)
    os.move.over(from, to)
  }

  def write(destPath: os.Path, content: String, perms: PermSet = null)(implicit
    buildOptions: NativeSettings
  ): Unit = {
    alreadyExistsCheck(destPath)
    os.write.over(destPath, content, perms)
  }

  lazy val executablePerms: PermSet = PermSet.fromString("rwxrwxr-x")

}
