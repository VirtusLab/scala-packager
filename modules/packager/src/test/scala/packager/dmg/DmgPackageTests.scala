package packager.dmg

import com.eed3si9n.expecty.Expecty.expect
import packager.{NativePackageHelper, TestUtils}
import packager.config.MacOSSettings
import packager.mac.dmg.DmgPackage

import scala.util.Properties

class DmgPackageTests extends munit.FunSuite with NativePackageHelper {
  override def munitFlakyOK: Boolean = Properties.isMac && !TestUtils.isAarch64

  override def outputPackagePath: os.Path = tmpDir / s"scalafmt.dmg"

  if (Properties.isMac) {
    def shouldCreateAppDirectoryForDmg(): Unit = {
      val dmgPackage = DmgPackage(buildSettings)

      // create app directory
      dmgPackage.createAppDirectory()

      val expectedAppDirectoryPath = tmpDir / s"$packageName.app"
      val expectedLauncherPath =
        expectedAppDirectoryPath / "Contents" / "MacOS" / packageName
      expect(os.isDir(expectedAppDirectoryPath))
      expect(os.isFile(expectedLauncherPath))
    }

    def shouldGenerateDmgPackage(): Unit = {
      val dmgPackage = DmgPackage(buildSettings)

      // create dmg package
      dmgPackage.build()

      expect(os.exists(outputPackagePath))
    }

    def shouldOverrideGeneratedDmgPackage(): Unit = {
      val dmgPackage = DmgPackage(buildSettings)

      // create twice dmg package
      dmgPackage.build()
      dmgPackage.build()

      expect(os.exists(outputPackagePath))
    }

    def sizeDmgPackageShouldBeSimilarToTheApp(): Unit = {
      val dmgPackage   = DmgPackage(buildSettings)
      val launcherSize = os.size(scalafmtLauncherPath)

      // create dmg package
      dmgPackage.build()

      expect(os.exists(outputPackagePath))

      val dmgPackageSize = os.size(outputPackagePath)

      expect(dmgPackageSize < launcherSize + (1024 * 1024))
    }

    if (TestUtils.isAarch64) {
      test("should create app directory for dmg") {
        shouldCreateAppDirectoryForDmg()
      }

      test("should generate dmg package") {
        shouldGenerateDmgPackage()
      }

      test("should override generated dmg package") {
        shouldOverrideGeneratedDmgPackage()
      }

      test("size dmg package should be similar to the app") {
        sizeDmgPackageShouldBeSimilarToTheApp()
      }
    }
    else {
      // flakiness on x86_64 MacOS is caused by hdiutil
      // FIXME: find a way to make this reliable
      // more context: https://github.com/VirtusLab/scala-cli/pull/2579)
      test("should create app directory for dmg".flaky) {
        shouldCreateAppDirectoryForDmg()
      }

      test("should generate dmg package".flaky) {
        shouldGenerateDmgPackage()
      }

      test("should override generated dmg package".flaky) {
        shouldOverrideGeneratedDmgPackage()
      }

      test("size dmg package should be similar to the app".flaky) {
        sizeDmgPackageShouldBeSimilarToTheApp()
      }
    }
  }

  override def buildSettings: MacOSSettings =
    MacOSSettings(
      shared = sharedSettings,
      identifier = s"org.scala.$packageName"
    )
}
