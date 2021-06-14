package packager.rpm

import com.eed3si9n.expecty.Expecty.expect
import packager.PackageHelper

import scala.util.Properties

class RedHatPackageTests extends munit.FunSuite with PackageHelper {

  if (Properties.isLinux) {
    test("should create rpmbuild directory ") {

      val rpmPackage = RedHatPackage(echoLauncherPath, buildOptions)

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

      val rpmPackage = RedHatPackage(echoLauncherPath, buildOptions)

      // create dmg package
      rpmPackage.build()

      println(s"$tmpDir rpm")
      val expectedDepPath = tmpDir / s"$packageName.rpm"
      expect(os.exists(expectedDepPath))
    }
  }

  override def extension: String = "rpm"
}