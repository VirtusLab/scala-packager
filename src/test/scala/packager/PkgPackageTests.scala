package packager

import packager.macOs.pkg.PkgPackage
import com.eed3si9n.expecty.Expecty.expect

class PkgPackageTests extends munit.FunSuite {

  val packageName = "echo"
  val buildOptions = BuildOptions(packageName, force = true)

  test("should create app directory") {

    val echoLauncherPath = Util.echoLauncher
    val pkgPackage = PkgPackage(echoLauncherPath, buildOptions)

    // create app directory
    pkgPackage.createAppDirectory()

    val expectedAppDirectoryPath = Util.tmpUtilDir / s"$packageName.app"
    val expectedEchoLauncherPath = expectedAppDirectoryPath / "Contents" / "MacOS" / packageName
    expect(os.isDir(expectedAppDirectoryPath))
    expect(os.isFile(expectedEchoLauncherPath))
  }

  test( "should generate deb package") {

    val packageName = "echo"
    val echoLauncherPath = Util.echoLauncher
    val pkgPackage = PkgPackage(echoLauncherPath, buildOptions)

    // create deb package
    pkgPackage.run()

    val expectedDebPath = Util.tmpUtilDir / s"$packageName.pkg"
    expect(os.isFile(expectedDebPath))

    // list files which will be installed
    val payloadFiles = os.proc("pkgutil", "--payload-files", expectedDebPath).call().out.text.trim
    val expectedAppPath = os.RelPath("./") / s"$packageName.app"
    val expectedEchoLauncherPath = expectedAppPath / "Contents" / "MacOS" / packageName

    expect(payloadFiles contains s"./$expectedAppPath")
    expect(payloadFiles contains s"./$expectedEchoLauncherPath")

  }
}
