package packager.dmg

case class DebianPackageInfo(
    packageName: String,
    version: String,
    maintainer: String,
    description: String,
    homepage: String
)
