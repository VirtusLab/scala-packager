package packager.deb

case class DebianMetaData(
    debianInfo: DebianPackageInfo,
    architecture: String = "all",
    depends: Seq[String] = Seq.empty
) {

  def generateContent(): String = {
    s"""Package: ${debianInfo.packageName}
    |Version: ${debianInfo.version}
    |Maintainer: ${debianInfo.maintainer}
    |Description: ${debianInfo.description}
    |Architecture: $architecture
    |""".stripMargin
  }

}
