package packager.config

trait BuildSettings {
  def shared: SharedSettings
}

case object BuildSettings {

  sealed trait PackageExtension
  case object Rpm extends PackageExtension
  case object Deb extends PackageExtension
  case object Pkg extends PackageExtension
  case object Dmg extends PackageExtension
  case object Msi extends PackageExtension
  case object Docker extends PackageExtension

}
