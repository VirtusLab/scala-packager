package packager.windows

import com.eed3si9n.expecty.Expecty.expect
import packager.PackageHelper
import packager.config.BuildSettings.{Msi, PackageExtension}

import scala.util.Properties

class WindowsPackageTests extends munit.FunSuite with PackageHelper {

  if (Properties.isWin) {

    test("should generate msi package") {

      val msiPackage = WindowsPackage(echoLauncherPath, buildOptions)

      // create msi package
      msiPackage.build()

      val expectedMsiPath = tmpDir / s"$packageName.msi"
      expect(os.exists(expectedMsiPath))
    }
  }

  test("should exists default licence file for msi package") {
    val msiPackage = WindowsPackage(echoLauncherPath, buildOptions)

    val licencePath = msiPackage.buildOptions.windows.licencePath

    expect(os.read(licencePath).nonEmpty)
  }

  override def extension: PackageExtension = Msi
}
