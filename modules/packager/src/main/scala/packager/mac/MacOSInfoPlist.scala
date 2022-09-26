package packager.mac

case class MacOSInfoPlist(
    executableName: String,
    identifier: String,
    hostArchitectures: List[String]
) {

  def archKey =
    if (hostArchitectures.nonEmpty) {
      s"""  <key>arch</key>
         |    <array>
         |      ${hostArchitectures.map(arch => s"<string>$arch</string>").mkString(System.lineSeparator())}
         |    </array>
         |""".stripMargin
    } else ""
  def generateContent: String = {
    val content = s"""<?xml version="1.0" encoding="UTF-8"?>
                     |<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
                     |<plist version="1.0">
                     |<dict>
                     |	<key>CFBundleExecutable</key>
                     |	<string>$executableName</string>
                     |	<key>CFBundleIdentifier</key>
                     |	<string>$identifier</string>
                     |  $archKey
                     |</dict>
                     |</plist>""".stripMargin
    content
  }

}
