package packager.pkg

import com.eed3si9n.expecty.Expecty.expect
import packager.NativePackageHelper
import packager.config.MacOSSettings
import packager.mac.pkg.PkgPackage

import scala.util.Properties

class PkgPackageTests extends munit.FunSuite with NativePackageHelper {

  override def outputPackagePath: os.Path = tmpDir / s"echo.pkg"

  if (Properties.isMac) {
    test("should create app directory") {

      val pkgPackage = PkgPackage(buildSettings)

      // create app directory
      pkgPackage.createAppDirectory()

      val expectedAppDirectoryPath = tmpDir / s"$packageName.app"
      val expectedEchoLauncherPath =
        expectedAppDirectoryPath / "Contents" / "MacOS" / packageName
      expect(os.isDir(expectedAppDirectoryPath))
      expect(os.isFile(expectedEchoLauncherPath))
    }

    test("should generate pkg package") {

      val pkgPackage = PkgPackage(buildSettings)

      // create pkg package
      pkgPackage.build()

      expect(os.isFile(outputPackagePath))

      // list files which will be installed
      val payloadFiles = os
        .proc("pkgutil", "--payload-files", outputPackagePath)
        .call()
        .out
        .text()
        .trim
      val expectedAppPath = os.RelPath(s"$packageName.app")
      val expectedEchoLauncherPath =
        expectedAppPath / "Contents" / "MacOS" / packageName

      expect(payloadFiles contains s"./$expectedAppPath")
      expect(payloadFiles contains s"./$expectedEchoLauncherPath")
    }

    test("should-install-pkg-package-with-architectures") {

      val buildSettingsArch = buildSettings.copy(
        hostArchitectures = List("arm64", "x86_64")
      )
      val pkgPackage = PkgPackage(buildSettingsArch)

      // create pkg package
      pkgPackage.build()
      println(outputPackagePath)
      // install pkg
//      os.proc(
//        "installer",
//        "-pkg",
//        outputPackagePath,
//        "-verbose",
//        "-target",
//        "CurrentUserHomeDirectory"
//      ).call(stdin = os.Inherit, stdout = os.Inherit)
//
//      val home = sys.props("user.home")
//      val echoInput = "Hello"
//      val output =
//        os.proc(
//          s"$home/Applications/$packageName.app/Contents/MacOS/$packageName",
//          echoInput
//        ).call(cwd = os.root)
//          .out
//          .trim()
//      expect(output == echoInput)
    }

    test("should override generated pkg package") {
      val pkgPackage = PkgPackage(buildSettings)

      // create twice pkg package
      pkgPackage.build()
      pkgPackage.build()

      expect(os.isFile(outputPackagePath))
    }

    test("should set given launcher name explicitly for pkg package") {

      val launcherApp = "launcher-test"

      val buildSettingsWithLauncherName: MacOSSettings = buildSettings.copy(
        shared = sharedSettings.copy(
          launcherApp = Some(launcherApp)
        )
      )

      val pkgPackage =
        PkgPackage(buildSettingsWithLauncherName)

      // create pkg package
      pkgPackage.build()

      expect(os.isFile(outputPackagePath))

      // list files which will be installed
      val payloadFiles = os
        .proc("pkgutil", "--payload-files", outputPackagePath)
        .call()
        .out
        .text()
        .trim
      val expectedAppPath = os.RelPath(s"$packageName.app")
      val expectedEchoLauncherPath =
        expectedAppPath / "Contents" / "MacOS" / launcherApp

      expect(payloadFiles contains s"./$expectedAppPath")
      expect(payloadFiles contains s"./$expectedEchoLauncherPath")

    }

    test("should copy post install script to pkg package") {

      val pkgPackage = PkgPackage(buildSettings)

      // create deb package
      pkgPackage.build()

      // expand the flat package pkg to directory
      val outPath = tmpDir / "out"
      os.proc("pkgutil", "--expand", outputPackagePath, outPath).call()

      val scriptsPath = outPath / "Scripts"
      val postInstallScriptPath = scriptsPath / "postinstall"

      expect(os.isDir(scriptsPath))
      expect(os.isFile(postInstallScriptPath))
    }
  }

  override def buildSettings: MacOSSettings =
    MacOSSettings(
      shared = sharedSettings,
      identifier = s"org.scala.$packageName",
      hostArchitectures = Nil
    )
}
