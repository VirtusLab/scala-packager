package packager.debian

import com.eed3si9n.expecty.Expecty.expect
import packager.PackageHelper
import packager.dmg.DebianPackage

import scala.util.Properties

class DebianPackageTests extends munit.FunSuite with PackageHelper {

  if (Properties.isLinux) {
    test("should create DEBIAN directory ") {

      val dmgPackage = DebianPackage(echoLauncherPath, buildOptions)

      // create app directory
      dmgPackage.createDebianDir()

      val debianDirectoryPath = tmpDir / s"$packageName-deb"
      val expectedAppDirectoryPath = debianDirectoryPath / "DEBIAN"
      val expectedEchoLauncherPath =
        debianDirectoryPath / "usr" / "share" / "scala" / packageName
      expect(os.isDir(expectedAppDirectoryPath))
      expect(os.isFile(expectedEchoLauncherPath))
    }

    test("should generate dep package") {

      val depPackage = DebianPackage(echoLauncherPath, buildOptions)

      // create dmg package
      depPackage.build()

      val expectedDepPath = tmpDir / s"$packageName.deb"
      expect(os.exists(expectedDepPath))

      // list files which will be installed
      val payloadFiles =
        os.proc("dpkg", "--contents", expectedDepPath).call().out.text().trim
      val expectedScriptPath = os.RelPath("usr") / "bin" / packageName
      val expectedEchoLauncherPath =
        os.RelPath("usr") / "share" / "scala" / packageName

      expect(payloadFiles contains s"./$expectedScriptPath")
      expect(payloadFiles contains s"./$expectedEchoLauncherPath")
    }
  }

  override def extension: String = "deb"
}
