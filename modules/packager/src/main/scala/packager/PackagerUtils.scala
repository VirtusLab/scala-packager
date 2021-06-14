package packager

import os.PermSet

object PackagerUtils {

  def alreadyExistsCheck(
      destPath: os.Path
  )(implicit buildOptions: BuildSettings): Unit =
    if (!buildOptions.force && os.exists(destPath)) {
      System.err.println(
        s"Error: $destPath already exists. Pass -f or --force to force erasing it."
      )
      sys.exit(1)
    }

  def osCopy(from: os.Path, to: os.Path)(implicit
      buildOptions: BuildSettings
  ): Unit = {
    alreadyExistsCheck(to)
    if (buildOptions.force) os.copy.over(from, to)
    else os.copy(from, to)
  }

  def osMove(from: os.Path, to: os.Path)(implicit
      buildOptions: BuildSettings
  ): Unit = {
    alreadyExistsCheck(to)
    if (buildOptions.force) os.move.over(from, to)
    else os.move(from, to)
  }

  def osWrite(destPath: os.Path, content: String, perms: PermSet = null)(
      implicit buildOptions: BuildSettings
  ): Unit = {
    alreadyExistsCheck(destPath)
    if (buildOptions.force) os.write.over(destPath, content, perms)
    else os.write(destPath, content, perms)
  }

  lazy val executablePerms: PermSet = PermSet.fromString("rwxrwxr-x")

}
