package packager.debian

case class DebianPackageInfo(
    packageName: String,
    version: String,
    maintainer: String,
    description: String,
    homepage: String
)
