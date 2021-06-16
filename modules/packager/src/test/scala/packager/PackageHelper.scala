package packager

trait PackageHelper {
  lazy val packageName = "echo"
  def extension: String
  lazy val tmpDir: os.Path = TestUtils.tmpUtilDir
  lazy val echoLauncherPath: os.Path = TestUtils.echoLauncher(tmpDir)
  lazy val outputPackagePath: os.Path = tmpDir / s"echo.$extension"
  lazy val buildOptions: BuildSettings = BuildSettings(
    force = true,
    workingDirectoryPath = Some(tmpDir),
    outputPath = outputPackagePath
  )
}
