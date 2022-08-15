package packager

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object TestUtils {

  def tmpUtilDir: os.Path = os.temp.dir(prefix = "scala-packager-tests")

  def echoLauncher(tmpDir: os.Path): os.Path = {
    val dest = tmpDir / "echo"
    os.proc("cs", "bootstrap", "-o", dest.toString, "echo-java").call()
    dest
  }

  def echoNative(tmpDir: os.Path): os.Path = {
    val dest = tmpDir / "echo-native"
    os.proc(
      "cs",
      "launch",
      "io.get-coursier:coursier-cli_2.12:2.1.0-M6-53-gb4f448130",
      "--",
      "bootstrap",
      "io.get-coursier:echo_native0.4_2.13:1.0.5",
      "-o",
      dest,
      "--native"
    ).call()
    dest
  }

  def logo(tmpDir: os.Path): os.Path = {
    val logoPath = tmpDir / "logo.png"
    val logo: BufferedImage =
      new BufferedImage(231, 250, BufferedImage.TYPE_INT_ARGB)
    ImageIO.write(logo, "png", new File(logoPath.toString()))
    logoPath
  }
}
