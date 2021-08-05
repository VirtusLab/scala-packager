package packager.windows

case object WindowsUtils {

  def convertLicenseToRtfFormat(licensePath: os.ReadablePath): String = {
    val license = os.read(licensePath)
    val rtfLicense =
      s"""{\\rtf1\\ansi{\\fonttbl{\\f0\\fcharset0 Times New Roman;}}
         |\\
         |${license.replaceAll("\n", "\\\\\n")}
         |}
         |""".stripMargin
    rtfLicense
  }

}
