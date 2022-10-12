package packager.windows

import com.eed3si9n.expecty.Expecty.expect
import packager.NativePackageHelper
import packager.config.WindowsSettings

import scala.util.Properties

class WindowsPackageTests extends munit.FunSuite with NativePackageHelper {

  override def outputPackagePath: os.Path = tmpDir / s"echo.msi"

  if (Properties.isWin) {

    test("should generate msi package") {

      val msiPackage = WindowsPackage(
        buildSettings,
        imageResizerOpt = Some(DefaultImageResizer)
      )

      // create msi package
      msiPackage.build()

      val expectedMsiPath = tmpDir / s"$packageName.msi"
      expect(os.exists(expectedMsiPath))
    }
    test("should override generated msi package") {

      val msiPackage = WindowsPackage(
        buildSettings,
        imageResizerOpt = Some(DefaultImageResizer)
      )

      // create twice msi package
      msiPackage.build()
      msiPackage.build()

      val expectedMsiPath = tmpDir / s"$packageName.msi"
      expect(os.exists(expectedMsiPath))
    }
  }

  test("should exists default licence file for msi package") {
    val msiPackage = WindowsPackage(
      buildSettings,
      imageResizerOpt = Some(DefaultImageResizer)
    )

    val licencePath = msiPackage.buildSettings.licencePath

    expect(os.read(licencePath).nonEmpty)
  }

  override def buildSettings: WindowsSettings =
    WindowsSettings(
      shared = sharedSettings,
      maintainer = "Scala Packager",
      licencePath = os.resource / "packager" / "apache-2.0",
      productName = "Scala packager product",
      exitDialog = None,
      suppressValidation = true,
      extraConfigs = Nil,
      is64Bits = false,
      installerVersion = None,
      wixUpgradeCodeGuid = None,
    )
}
