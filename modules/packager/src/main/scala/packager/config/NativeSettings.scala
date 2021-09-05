package packager.config

trait NativeSettings extends BuildSettings {
  def shared: SharedSettings
}
