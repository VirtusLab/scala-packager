package packager.windows.wix

trait WixComponent {
  def generate: String
}

case class WixVariable(id: WixVariableId, value: String) extends WixComponent {
  override def generate: String = s"<WixVariable Id=\"$id\" Value=\"$value\" />"
}

case class Property(id: PropertyId, value: String) extends WixComponent {
  override def generate: String = s"<Property Id=\"$id\" Value=\"$value\" />"
}

case class UIRef(id: UIRefId) extends WixComponent {
  override def generate: String = s"<UIRef Id=\"$id\" />"
}

case class ComponentRef(id: ComponentRefId) extends WixComponent {
  override def generate: String = s"<ComponentRef Id=\"$id\" />"
}

case class Feature(id: FeatureID, title: String, description: String, display: Option[String], level: Int, configurableDirectory: Option[String], components: List[WixComponent])