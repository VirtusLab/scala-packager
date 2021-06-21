package packager.deb

case class DebianMetaData(
    debianInfo: DebianPackageInfo,
    architecture: String = "all",
    dependsOn: List[String] = Nil,
    conflicts: List[String] = Nil
) {

  def generateContent(): String = {
    s"""Package: ${debianInfo.packageName}
    |Version: ${debianInfo.version}
    |Maintainer: ${debianInfo.maintainer}
    |Description: ${debianInfo.description}
    |Architecture: $architecture
    |${if (dependsOn.nonEmpty) "Depends: " + dependsOn.mkString(", ") else ""}
    |${if (conflicts.nonEmpty) "Conflicts: " + dependsOn.mkString(", ") else ""}
    |""".stripMargin
  }

}
