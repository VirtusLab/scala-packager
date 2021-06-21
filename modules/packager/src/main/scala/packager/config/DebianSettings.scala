package packager.config

case class DebianSettings(
    debianConflicts: List[String],
    debianDependencies: List[String],
    architecture: String
)

case object DebianSettings {
  val default: DebianSettings = DebianSettings(
    debianConflicts = Nil,
    debianDependencies = Nil,
    architecture = "all"
  )
}
