package packager

trait PackageHelper {
  val packageName = "echo"
  val tmpDir = TestUtils.tmpUtilDir
  val echoLauncherPath = TestUtils.echoLauncher(tmpDir)
  val buildOptions = BuildOptions(packageName, force = true, workingDirPath = Some(tmpDir), outputPath = Some(tmpDir))
}
