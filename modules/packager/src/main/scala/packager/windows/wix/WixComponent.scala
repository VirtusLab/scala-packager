package packager.windows.wix

trait WixComponent {
  def generate: String
}

case class WixVariable(id: WixVariableId, value: String) extends WixComponent {
  override def generate: String =
    s"""<WixVariable Id=\"$id\" Value=\"$value\" />"""
}

case class Property(id: PropertyId, value: String) extends WixComponent {
  override def generate: String =
    s"""<Property Id=\"$id\" Value=\"$value\" />"""
}

case class Icon(id: String, sourceFile: String) extends WixComponent {
  override def generate: String =
    s"""<Icon Id=\"$id\" SourceFile=\"$sourceFile\" />"""
}
