package packager.deb

import com.eed3si9n.expecty.Expecty.expect
import packager.PackageHelper
import packager.config.DebianSettings
import packager.config.BuildSettings.{Deb, PackageExtension}

import scala.util.Properties

class DebianPackageTests extends munit.FunSuite with PackageHelper {

  if (Properties.isLinux) {
    test("should create DEBIAN directory ") {
      val dmgPackage = DebianPackage(echoLauncherPath, buildSettings)

      // create app directory
      dmgPackage.createDebianDir()

      val debianDirectoryPath = tmpDir / "debian"
      val expectedAppDirectoryPath = debianDirectoryPath / "DEBIAN"
      val expectedEchoLauncherPath =
        debianDirectoryPath / "usr" / "share" / "scala" / packageName
      expect(os.isDir(expectedAppDirectoryPath))
      expect(os.isFile(expectedEchoLauncherPath))
    }

    test("should generate dep package") {

      val depPackage = DebianPackage(echoLauncherPath, buildSettings)

      // create dmg package
      depPackage.build()

      expect(os.exists(outputPackagePath))

      // list files which will be installed
      val payloadFiles =
        os.proc("dpkg", "--contents", outputPackagePath).call().out.text().trim
      val expectedScriptPath = os.RelPath("usr") / "bin" / packageName
      val expectedEchoLauncherPath =
        os.RelPath("usr") / "share" / "scala" / packageName

      expect(payloadFiles contains s"./$expectedScriptPath")
      expect(payloadFiles contains s"./$expectedEchoLauncherPath")
    }
  }

  override def extension: PackageExtension = Deb

  override def buildSettings: DebianSettings =
    DebianSettings(
      shared = sharedSettings,
      version = "1.0.0",
      maintainer = "Scala Packager",
      description = "Scala Packager Test",
      debianConflicts = Nil,
      debianDependencies = Nil,
      architecture = "all"
    )
}
