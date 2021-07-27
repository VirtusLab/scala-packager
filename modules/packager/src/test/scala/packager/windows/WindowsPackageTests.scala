package packager.windows

import com.eed3si9n.expecty.Expecty.expect
import packager.PackageHelper
import packager.config.BuildSettings.{Msi, PackageExtension}
import packager.config.WindowsSettings

import scala.util.Properties

class WindowsPackageTests extends munit.FunSuite with PackageHelper {

  if (Properties.isWin) {

    test("should generate msi package") {

      val msiPackage = WindowsPackage(echoLauncherPath, buildSettings)

      // create msi package
      msiPackage.build()

      val expectedMsiPath = tmpDir / s"$packageName.msi"
      expect(os.exists(expectedMsiPath))
    }
  }

  test("should exists default licence file for msi package") {
    val msiPackage = WindowsPackage(echoLauncherPath, buildSettings)

    val licencePath = msiPackage.buildSettings.licencePath

    expect(os.read(licencePath).nonEmpty)
  }

  override def extension: PackageExtension = Msi

  override def buildSettings: WindowsSettings =
    WindowsSettings(
      shared = sharedSettings,
      maintainer = "Scala Packager",
      licencePath = os.resource / "packager" / "apache-2.0",
      productName = "Scala packager product",
      exitDialog = None
    )
}
