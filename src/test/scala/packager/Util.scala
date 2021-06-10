package packager

object Util {

  lazy val tmpUtilDir: os.Path = os.temp.dir(prefix = "scala-packager-tests")

  lazy val echoLauncher: os.Path = {
    val dest = tmpUtilDir / "echo"
    os.proc("cs", "bootstrap", "-o", dest.toString, "echo-java").call()
    dest
  }
}
