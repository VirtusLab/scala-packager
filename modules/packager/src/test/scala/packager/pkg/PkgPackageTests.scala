package packager.pkg

import com.eed3si9n.expecty.Expecty.expect
import packager.PackageHelper
import packager.macOs.pkg.PkgPackage

import scala.util.Properties

class PkgPackageTests extends munit.FunSuite with PackageHelper {

  if (Properties.isMac) {
    test("should create app directory") {

      val pkgPackage = PkgPackage(echoLauncherPath, buildOptions)

      // create app directory
      pkgPackage.createAppDirectory()

      val expectedAppDirectoryPath = tmpDir / s"$packageName.app"
      val expectedEchoLauncherPath =
        expectedAppDirectoryPath / "Contents" / "MacOS" / packageName
      expect(os.isDir(expectedAppDirectoryPath))
      expect(os.isFile(expectedEchoLauncherPath))
    }

    test("should generate pkg package") {

      val pkgPackage = PkgPackage(echoLauncherPath, buildOptions)

      // create pkg package
      pkgPackage.build()

      val expectedPkgPath = tmpDir / s"$packageName.pkg"
      expect(os.isFile(expectedPkgPath))

      // list files which will be installed
      val payloadFiles = os
        .proc("pkgutil", "--payload-files", expectedPkgPath)
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

    test("should copy post install script to pkg package") {

      val pkgPackage = PkgPackage(echoLauncherPath, buildOptions)

      // create deb package
      pkgPackage.build()

      val expectedDebPath = tmpDir / s"$packageName.pkg"
      expect(os.isFile(expectedDebPath))

      // expand the flat package pkg to directory
      val outPath = tmpDir / "out"
      os.proc("pkgutil", "--expand", expectedDebPath, outPath).call()

      val scriptsPath = outPath / "Scripts"
      val postInstallScriptPath = scriptsPath / "postinstall"

      expect(os.isDir(scriptsPath))
      expect(os.isFile(postInstallScriptPath))
    }
  }

  override def extension: String = "pkg"
}
