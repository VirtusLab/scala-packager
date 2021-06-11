import sbt.{Project, file}

object Settings {
  def project(id: String) =
    Project(id, file(s"modules/$id"))
}
