package packager

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object TestUtils {
  lazy val isAarch64: Boolean = sys.props.get("os.arch").contains("aarch64")

  def scalafmtVersion = "3.9.1"

  def tmpUtilDir: os.Path = os.temp.dir(prefix = "scala-packager-tests")

  def scalafmtNative(tmpDir: os.Path): os.Path = {
    val dest = tmpDir / "scalafmt-native"
    os.proc(
      "curl",
      "-L",
      "-o",
      dest,
      s"https://github.com/scalameta/scalafmt/releases/download/v$scalafmtVersion/scalafmt-x86_64-pc-linux"
    ).call()
    dest
  }

  def scalafmtLauncher(tmpDir: os.Path): os.Path = {
    val dest = tmpDir / "scalafmt"
    os.proc(
      "cs",
      "bootstrap",
      "-o",
      dest.toString,
      s"org.scalameta:scalafmt-cli_2.13:$scalafmtVersion"
    ).call()
    dest
  }

  def logo(tmpDir: os.Path): os.Path = {
    val logoPath            = tmpDir / "logo.png"
    val logo: BufferedImage =
      new BufferedImage(231, 250, BufferedImage.TYPE_INT_ARGB)
    ImageIO.write(logo, "png", new File(logoPath.toString()))
    logoPath
  }
}
