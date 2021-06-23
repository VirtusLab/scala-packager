package packager.windows

case object WindowsUtils {

  lazy val rtfHeaderPath: os.ResourcePath =
    os.resource / "packager" / "common" / "rtf-header"
  lazy val rtfHeader: String = os.read(rtfHeaderPath)

  def convertLicenseToRtfFormat(licensePath: os.ReadablePath): String = {
    val license = os.read(licensePath)
    val rtfLicense =
      s"""$rtfHeader
         |\\
         |${license.replaceAll("\n", "\\\\\n")}
         |""".stripMargin
    rtfLicense
  }

}
