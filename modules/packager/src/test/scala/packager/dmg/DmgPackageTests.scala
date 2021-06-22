package packager.dmg

import com.eed3si9n.expecty.Expecty.expect
import packager.PackageHelper
import packager.config.BuildSettings.{Dmg, PackageExtension}
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
    test("size dmg package should be similar to the app") {

      val dmgPackage = DmgPackage(echoLauncherPath, buildOptions)
      val echoLauncherSize = os.size(echoLauncherPath)

      // create dmg package
      dmgPackage.build()

      expect(os.exists(outputPackagePath))

      val dmgPackageSize = os.size(outputPackagePath)
      // dmgPackageSize < echoLauncherSize +  1Mb
      expect(dmgPackageSize < echoLauncherSize + (1024 * 1024))
    }
  }

  override def extension: PackageExtension = Dmg
}
