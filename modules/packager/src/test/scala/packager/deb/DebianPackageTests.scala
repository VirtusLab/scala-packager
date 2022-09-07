package packager.deb

import com.eed3si9n.expecty.Expecty.expect
import packager.NativePackageHelper
import packager.config.DebianSettings

import scala.util.Properties

class DebianPackageTests extends munit.FunSuite with NativePackageHelper {

  override def outputPackagePath: os.Path = tmpDir / s"echo.deb"

  if (Properties.isLinux) {
    test("should create DEBIAN directory ") {
      val dmgPackage = DebianPackage(buildSettings)

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

      val depPackage = DebianPackage(buildSettings)

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

    test("should override generated dep package") {

      val depPackage = DebianPackage(buildSettings)

      // create twice dmg package
      depPackage.build()
      depPackage.build()

      expect(os.exists(outputPackagePath))
    }

    test("should set given launcher name explicitly for debian package") {

      val launcherApp = "launcher-test"

      val buildSettingsWithLauncherName: DebianSettings = buildSettings.copy(
        shared = sharedSettings.copy(
          launcherApp = Some(launcherApp)
        )
      )

      val depPackage =
        DebianPackage(buildSettingsWithLauncherName)

      // create dmg package
      depPackage.build()

      expect(os.exists(outputPackagePath))

      // list files which will be installed
      val payloadFiles =
        os.proc("dpkg", "--contents", outputPackagePath).call().out.text().trim
      val expectedScriptPath = os.RelPath("usr") / "bin" / launcherApp
      val expectedEchoLauncherPath =
        os.RelPath("usr") / "share" / "scala" / launcherApp

      expect(payloadFiles contains s"./$expectedScriptPath")
      expect(payloadFiles contains s"./$expectedEchoLauncherPath")
    }

    test("should contain priority and section flags") {

      val depPackage = DebianPackage(buildSettings)

      // create dmg package
      depPackage.build()

      // list files which will be installed
      val payloadFiles = os.proc("dpkg", "--info", outputPackagePath).call().out.text().trim

      expect(payloadFiles contains "Priority: optional")
      expect(payloadFiles contains "Section: devel")
    }
  }

  override def buildSettings: DebianSettings =
    DebianSettings(
      shared = sharedSettings,
      maintainer = "Scala Packager",
      description = "Scala Packager Test",
      debianConflicts = Nil,
      debianDependencies = Nil,
      architecture = "all",
      priority = Some("optional"),
      section = Some("devel")
    )
}
