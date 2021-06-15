package packager

trait PackageHelper {
  val packageName = "echo"
  def extension: String
  val tmpDir: os.Path = TestUtils.tmpUtilDir
  val echoLauncherPath: os.Path = TestUtils.echoLauncher(tmpDir)
  val outputPackagePath: os.Path = tmpDir / s"echo.$extension"
  val buildOptions: BuildSettings = BuildSettings(
    force = true,
    workingDirectoryPath = Some(tmpDir),
    outputPath = outputPackagePath
  )
}
