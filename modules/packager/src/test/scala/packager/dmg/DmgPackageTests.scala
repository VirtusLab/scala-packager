package packager.dmg

import com.eed3si9n.expecty.Expecty.expect
import packager.NativePackageHelper
import packager.config.MacOSSettings
import packager.mac.dmg.DmgPackage

import scala.util.Properties

class DmgPackageTests extends munit.FunSuite with NativePackageHelper {

  override def outputPackagePath: os.Path = tmpDir / s"scalafmt.dmg"

  if (Properties.isMac) {
    test("should create app directory for dmg") {

      val dmgPackage = DmgPackage(buildSettings)

      // create app directory
      dmgPackage.createAppDirectory()

      val expectedAppDirectoryPath = tmpDir / s"$packageName.app"
      val expectedLauncherPath =
        expectedAppDirectoryPath / "Contents" / "MacOS" / packageName
      expect(os.isDir(expectedAppDirectoryPath))
      expect(os.isFile(expectedLauncherPath))
    }

    test("should generate dmg package") {

      val dmgPackage = DmgPackage(buildSettings)

      // create dmg package
      dmgPackage.build()

      expect(os.exists(outputPackagePath))
    }

    test("should override generated dmg package") {

      val dmgPackage = DmgPackage(buildSettings)

      // create twice dmg package
      dmgPackage.build()
      dmgPackage.build()

      expect(os.exists(outputPackagePath))
    }

    test("size dmg package should be similar to the app") {

      val dmgPackage   = DmgPackage(buildSettings)
      val launcherSize = os.size(scalafmtLauncherPath)

      // create dmg package
      dmgPackage.build()

      expect(os.exists(outputPackagePath))

      val dmgPackageSize = os.size(outputPackagePath)

      expect(dmgPackageSize < launcherSize + (1024 * 1024))
    }
  }

  override def buildSettings: MacOSSettings =
    MacOSSettings(
      shared = sharedSettings,
      identifier = s"org.scala.$packageName"
    )
}
