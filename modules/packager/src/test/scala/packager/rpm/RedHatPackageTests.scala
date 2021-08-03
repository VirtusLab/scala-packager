package packager.rpm

import com.eed3si9n.expecty.Expecty.expect
import packager.PackageHelper
import packager.config.BuildSettings.{PackageExtension, Rpm}
import packager.config.RedHatSettings

import scala.util.Properties

class RedHatPackageTests extends munit.FunSuite with PackageHelper {

  if (Properties.isLinux) {
    test("should create rpmbuild directory ") {

      val rpmPackage = RedHatPackage(echoLauncherPath, buildSettings)

      // create app directory
      rpmPackage.createRedHatDir()

      val rpmDirectoryPath = tmpDir / "rpmbuild"
      val expectedAppDirectoryPath = rpmDirectoryPath / "SOURCES"
      val expectedEchoLauncherPath =
        expectedAppDirectoryPath / packageName
      expect(os.isDir(expectedAppDirectoryPath))
      expect(os.isFile(expectedEchoLauncherPath))
    }

    test("should generate rpm package") {

      val rpmPackage = RedHatPackage(echoLauncherPath, buildSettings)

      // create dmg package
      rpmPackage.build()

      val expectedRpmPath = tmpDir / s"$packageName.rpm"
      expect(os.exists(expectedRpmPath))

      // list files which will be installed
      val payloadFiles =
        os.proc("rpm", "-qpl", expectedRpmPath).call().out.text().trim
      val expectedEchoLauncherPath =
        os.RelPath("usr") / "bin" / packageName

      expect(payloadFiles contains s"/$expectedEchoLauncherPath")
    }
  }

  override def extension: PackageExtension = Rpm

  override def buildSettings: RedHatSettings =
    RedHatSettings(
      shared = sharedSettings,
      description = "Scala Packager Test",
      license = "ASL 2.0",
      release = "1",
      rpmArchitecture = "noarch"
    )
}
