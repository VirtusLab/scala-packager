package packager.windows

import com.eed3si9n.expecty.Expecty.expect
import packager.PackageHelper

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

  override def extension: String = "msi"
}
