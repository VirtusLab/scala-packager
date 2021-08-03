package packager.windows

import com.eed3si9n.expecty.Expecty.expect
import packager.TestUtils

class WixLogoSpec extends munit.FunSuite {

  private lazy val workDirPath = TestUtils.tmpUtilDir
  private lazy val logoPath: os.Path = TestUtils.logo(workDirPath)

  test("should prepare icon for wix installer") {
    val iconPath = WindowsUtils.generateIcon(logoPath, workDirPath)

    val expectedIconPath = workDirPath / "logo.ico"

    assertEquals(iconPath, expectedIconPath)
    expect(os.exists(expectedIconPath))
  }

  test("should prepare banner for wix installer") {
    val bannerPath = WindowsUtils.generateBanner(logoPath, workDirPath)

    val expectedBannerPath = workDirPath / "banner.bmp"

    assertEquals(bannerPath, expectedBannerPath)
    expect(os.exists(expectedBannerPath))
  }

  test("should prepare dialog for wix installer") {

    val dialogPath = WindowsUtils.generateDialog(logoPath, workDirPath)

    val expectedDialogPath = workDirPath / "dialog.bmp"

    assertEquals(dialogPath, expectedDialogPath)
    expect(os.exists(expectedDialogPath))
  }
}
