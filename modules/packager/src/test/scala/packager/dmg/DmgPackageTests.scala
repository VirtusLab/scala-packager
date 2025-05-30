package packager.dmg

import com.eed3si9n.expecty.Expecty.expect
import munit.Tag
import packager.{NativePackageHelper, TestUtils}
import packager.config.MacOSSettings
import packager.mac.dmg.DmgPackage

import scala.util.Properties

class DmgPackageTests extends munit.FunSuite with NativePackageHelper {
  override def munitFlakyOK: Boolean = true

  private val FlakyMacOSx86_64 = new Tag("FlakyMacOSx86_64")

  // flakiness on x86_64 MacOS is caused by hdiutil
  // FIXME: find a way to make these reliable
  // more context: https://github.com/VirtusLab/scala-cli/pull/2579
  override def munitTestTransforms: List[TestTransform] =
    List(
      new TestTransform(
        "Flaky MacOS x86_64",
        { test =>
          if (test.tags.contains(FlakyMacOSx86_64) && Properties.isMac && !TestUtils.isAarch64)
            test.tag(munit.Flaky)
          else test
        }
      )
    ) ++ super.munitTestTransforms

  override def outputPackagePath: os.Path = tmpDir / s"scalafmt.dmg"

  def shouldCreateAppDirectoryForDmg(): Unit = {
    val dmgPackage = DmgPackage(buildSettings)

    // create app directory
    dmgPackage.createAppDirectory()

    val expectedAppDirectoryPath = tmpDir / s"$packageName.app"
    val expectedLauncherPath     =
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

  if (Properties.isMac) {
    test("should create app directory for dmg".tag(FlakyMacOSx86_64)) {
      shouldCreateAppDirectoryForDmg()
    }

    test("should generate dmg package".tag(FlakyMacOSx86_64)) {
      shouldGenerateDmgPackage()
    }

    test("should override generated dmg package".tag(FlakyMacOSx86_64)) {
      shouldOverrideGeneratedDmgPackage()
    }

    test("size dmg package should be similar to the app".tag(FlakyMacOSx86_64)) {
      sizeDmgPackageShouldBeSimilarToTheApp()
    }
  }

  override def buildSettings: MacOSSettings =
    MacOSSettings(
      shared = sharedSettings,
      identifier = s"org.scala.$packageName"
    )
}
