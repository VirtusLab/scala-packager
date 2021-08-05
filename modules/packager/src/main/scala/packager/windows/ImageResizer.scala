package packager.windows

trait ImageResizer {
  def generateIcon(logoPath: os.Path, workDirPath: os.Path): os.Path
  def generateBanner(logoPath: os.Path, workDirPath: os.Path): os.Path
  def generateDialog(logoPath: os.Path, workDirPath: os.Path): os.Path
}
