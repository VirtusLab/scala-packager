package packager.deb

case class DebianPackageInfo(
  packageName: String,
  version: String,
  maintainer: String,
  description: String
)
