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

  def logo(tmpDir: os.Path): os.Path = {
    val logoPath = tmpDir / "logo.png"
    val logo: BufferedImage =
      new BufferedImage(231, 250, BufferedImage.TYPE_INT_ARGB)
    ImageIO.write(logo, "png", new File(logoPath.toString()))
    logoPath
  }
}
