package packager.rpm

import com.eed3si9n.expecty.Expecty.expect
import packager.NativePackageHelper
import packager.config.RedHatSettings

import scala.util.Properties

class RedHatPackageTests extends munit.FunSuite with NativePackageHelper {

  override def outputPackagePath: os.Path = tmpDir / s"echo.rpm"

  if (Properties.isLinux) {
    test("should create rpmbuild directory ") {

      val rpmPackage = RedHatPackage(buildSettings)

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

      val rpmPackage = RedHatPackage(buildSettings)

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

    test("should override generated rpm package") {

      val rpmPackage = RedHatPackage(buildSettings)

      // create twice dmg package
      rpmPackage.build()
      rpmPackage.build()

      val expectedRpmPath = tmpDir / s"$packageName.rpm"
      expect(os.exists(expectedRpmPath))
    }

    test("should set given launcher name explicitly for redhat package") {

      val launcherApp = "launcher-test"

      val buildSettingsWithLauncherName: RedHatSettings = buildSettings.copy(
        shared = sharedSettings.copy(
          launcherApp = Some(launcherApp)
        )
      )

      val rpmPackage = RedHatPackage(buildSettingsWithLauncherName)

      // create rpm package
      rpmPackage.build()

      // list files which will be installed
      val payloadFiles =
        os.proc("rpm", "-qpl", outputPackagePath).call().out.text().trim
      val expectedEchoLauncherPath =
        os.RelPath("usr") / "bin" / launcherApp

      expect(payloadFiles contains s"/$expectedEchoLauncherPath")
    }
  }

  override def buildSettings: RedHatSettings =
    RedHatSettings(
      shared = sharedSettings,
      description = "Scala Packager Test",
      license = "ASL 2.0",
      release = "1",
      rpmArchitecture = "noarch"
    )
}
