package packager.windows

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.filters.Canvas
import net.coobird.thumbnailator.geometry.Positions
import net.sf.image4j.codec.ico.ICOEncoder

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

case object DefaultImageResizer extends ImageResizer {

  def generateIcon(logoPath: os.Path, workDirPath: os.Path): os.Path = {
    val icoTmpPath = workDirPath / "logo_tmp.ico"
    val iconImage: BufferedImage = ImageIO.read(new File(logoPath.toString()));
    ICOEncoder.write(iconImage, new File(icoTmpPath.toString()));
    val icoPath = workDirPath / "logo.ico"
    os.copy.over(icoTmpPath, icoPath)
    icoPath
  }

  private def generateEmptyImage(width: Int, height: Int): BufferedImage =
    new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

  private def resizeLogo(
      logoPath: os.Path,
      width: Int,
      height: Int,
      workDir: os.Path
  ) = {
    val resizedPath = workDir / "resized-logo.png"
    Thumbnails
      .of(logoPath.toString())
      .size(width, height)
      .toFile(new File(resizedPath.toString()))

    resizedPath
  }

  def generateBanner(logoPath: os.Path, workDirPath: os.Path): os.Path = {
    val emptyBanner = generateEmptyImage(493, 58)

    val bannerPath = workDirPath / s"banner.bmp"
    val resizedLogoPath = resizeLogo(logoPath, 55, 55, workDirPath)

    Thumbnails
      .of(emptyBanner)
      .addFilter(new Canvas(493, 58, Positions.CENTER, Color.WHITE))
      .size(493, 58)
      .watermark(
        Positions.CENTER_RIGHT,
        ImageIO.read(new File(resizedLogoPath.toString())),
        1
      )
      .toFile(new File(bannerPath.toString()));

    bannerPath
  }

  def generateDialog(logoPath: os.Path, workDirPath: os.Path): os.Path = {

    val emptyDialog = generateEmptyImage(493, 312)
    val dialogPath = workDirPath / s"dialog.bmp"

    val resizedLogoPath = resizeLogo(logoPath, 165, 330, workDirPath)

    Thumbnails
      .of(emptyDialog)
      .size(493, 312)
      .addFilter(new Canvas(493, 312, Positions.CENTER, Color.WHITE))
      .watermark(
        Positions.CENTER_LEFT,
        ImageIO.read(new File(resizedLogoPath.toString())),
        1
      )
      .toFile(new File(dialogPath.toString()));

    dialogPath
  }

}
