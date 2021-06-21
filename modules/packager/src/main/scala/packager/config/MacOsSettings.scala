package packager.config

case class MacOsSettings(identifier: String)

case object MacOsSettings {
  val default = MacOsSettings(
    identifier = "org.scala"
  )
}
