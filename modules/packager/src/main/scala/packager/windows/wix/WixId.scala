package packager.windows.wix

sealed trait WixId

sealed trait WixVariableId extends WixId

case object WixUIBannerBmp extends WixVariableId
case object WixUIDialogBmp extends WixVariableId

sealed trait PropertyId extends WixId

case object WIXUI_EXITDIALOGOPTIONALTEXT extends PropertyId
case object ARPPRODUCTICON               extends PropertyId
