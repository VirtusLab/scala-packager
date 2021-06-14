package packager

trait PackageHelper {
  val packageName = "echo"
  def extension: String
  val tmpDir = TestUtils.tmpUtilDir
  val echoLauncherPath = TestUtils.echoLauncher(tmpDir)
  val buildOptions = BuildSettings(
    force = true,
    workingDirectoryPath = Some(tmpDir),
    outputPath = tmpDir / s"echo.$extension"
  )
}
