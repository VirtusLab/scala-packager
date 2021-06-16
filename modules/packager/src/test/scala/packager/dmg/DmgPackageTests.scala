package packager.dmg

import com.eed3si9n.expecty.Expecty.expect
import packager.PackageHelper
import packager.mac.dmg.DmgPackage

import scala.util.Properties

class DmgPackageTests extends munit.FunSuite with PackageHelper {

  if (Properties.isMac) {
    test("should create app directory for dmg") {

      val dmgPackage = DmgPackage(echoLauncherPath, buildOptions)

      // create app directory
      dmgPackage.createAppDirectory()

      val expectedAppDirectoryPath = tmpDir / s"$packageName.app"
      val expectedEchoLauncherPath =
        expectedAppDirectoryPath / "Contents" / "MacOS" / packageName
      expect(os.isDir(expectedAppDirectoryPath))
      expect(os.isFile(expectedEchoLauncherPath))
    }

    test("should generate dmg package") {

      val dmgPackage = DmgPackage(echoLauncherPath, buildOptions)

      // create dmg package
      dmgPackage.build()

      expect(os.exists(outputPackagePath))
    }
  }

  override def extension: String = "dmg"
}
