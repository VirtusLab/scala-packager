package packager

object TestUtils {

  def tmpUtilDir: os.Path = os.temp.dir(prefix = "scala-packager-tests")

  def echoLauncher(tmpDir: os.Path): os.Path = {
    val dest = tmpDir / "echo"
    os.proc("cs", "bootstrap", "-o", dest.toString, "echo-java").call()
    dest
  }
}
