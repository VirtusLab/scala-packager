package packager.deb

case class DebianMetaData(
  debianInfo: DebianPackageInfo,
  architecture: String = "all",
  dependsOn: List[String] = Nil,
  conflicts: List[String] = Nil,
  priority: Option[String],
  section: Option[String]
) {

  def generateContent(): String = {
    val flags = List(
      s"Package: ${debianInfo.packageName}",
      s"Version: ${debianInfo.version}",
      s"Maintainer: ${debianInfo.maintainer}",
      s"Description: ${debianInfo.description}",
      s"Architecture: $architecture"
    ) ++
      (if (priority.nonEmpty) List("Priority: " + priority.getOrElse("")) else Nil) ++
      (if (section.nonEmpty) List("Section: " + section.getOrElse("")) else Nil) ++
      (if (dependsOn.nonEmpty) List("Depends: " + dependsOn.mkString(", ")) else Nil) ++
      (if (conflicts.nonEmpty) List("Conflicts: " + conflicts.mkString(", ")) else Nil)

    flags.mkString("", System.lineSeparator(), System.lineSeparator())
  }

}
