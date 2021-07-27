package packager.windows.wix

sealed trait WixId

sealed trait WixVariableId extends WixId

case object WixUIBannerBmp extends WixVariableId
case object WixUIDialogBmp extends WixVariableId
case object WixUILicenseRtf extends WixVariableId

sealed trait PropertyId extends WixId

case object WIXUI_EXITDIALOGOPTIONALTEXT extends PropertyId
case object WIXUI_INSTALLDIR extends PropertyId

sealed trait UIRefId extends WixId
case object WixUI_InstallDir extends UIRefId

sealed trait ComponentRefId extends WixId

case object ApplicationFiles extends ComponentRefId
case object setEnviroment extends ComponentRefId

sealed trait FeatureID extends WixId
case object Complete extends FeatureID
case object MainProgram extends FeatureID
