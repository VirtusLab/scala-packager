package packager.cli.commands

import caseapp.core.help.Help
import caseapp.{Group, HelpMessage, Parser, ValueDescription}
import SettingsHelpers.{Mandatory, Validate}
import packager.config.{SharedSettings, WindowsSettings}

import java.nio.charset.Charset

import scala.io.Codec

final case class WindowsOptions(
    @Group("Windows")
    @HelpMessage("Path to license file")
    licensePath: Option[String] = None,
    @Group("Windows")
    @HelpMessage("Name of product, default: Scala packager")
    productName: String = "Scala packager",
    @Group("Windows")
    @HelpMessage("Text will be displayed on exit dialog")
    exitDialog: Option[String] = None,
    @Group("Windows")
    @HelpMessage("Suppress Wix ICE validation (required for users that are neither interactive, not local administrators)")
    suppressValidation: Boolean = false,
    @Group("Windows")
    @HelpMessage("Path to extra WIX config content")
    @ValueDescription("path")
    extraConfig: List[String] = Nil
) {

  def toWindowsSettings(
      sharedSettings: SharedSettings,
      maintainer: Option[String]
  ): WindowsSettings =
    WindowsSettings(
      shared = sharedSettings,
      maintainer = maintainer.mandatory(
        "Maintainer parameter is mandatory for debian package"
      ),
      licencePath = os.Path(
        licensePath.mandatory(
          "License path parameter is mandatory for windows packages"
        ),
        os.pwd
      ),
      productName = productName,
      exitDialog = exitDialog,
      suppressValidation = suppressValidation,
      extraConfig =
        if (extraConfig.isEmpty) None
        else
          Some {
            extraConfig
              .map { path =>
                val path0 = os.Path(path, os.pwd)
                os.read(path0, Codec(Charset.defaultCharset()))
              }
              .mkString(System.lineSeparator())
          }
    )
}

case object WindowsOptions {

  implicit val parser = Parser[WindowsOptions]
  implicit val help = Help[WindowsOptions]
}
