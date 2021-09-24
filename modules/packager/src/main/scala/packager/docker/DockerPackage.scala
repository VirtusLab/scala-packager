package packager.docker

import com.google.cloud.tools.jib.api.{
  Containerizer,
  DockerDaemonImage,
  ImageReference,
  Jib
}
import com.google.cloud.tools.jib.api.buildplan.{
  AbsoluteUnixPath,
  FileEntriesLayer,
  FilePermissions
}
import packager.Packager
import packager.config.DockerSettings

import java.time.Instant

case class DockerPackage(sourceAppPath: os.Path, buildSettings: DockerSettings)
    extends Packager {

  override def build(): Unit = {

    lazy val targetImageReference: ImageReference =
      ImageReference.of(
        buildSettings.registry.orNull,
        buildSettings.repository,
        buildSettings.tag.orNull
      )

    val targetImage = DockerDaemonImage.named(targetImageReference)
    val entrypoint = buildSettings.exec
      .map(e => List(s"$e", s"/$launcherApp"))
      .getOrElse(List(s"/$launcherApp"))

    def makeFileEntryLayerConfiguration(
        resourcePath: os.Path,
        dest: String
    ): FileEntriesLayer = {
      val layerConfigurationBuilder = FileEntriesLayer.builder
      layerConfigurationBuilder.addEntry(
        resourcePath.toNIO,
        AbsoluteUnixPath.get(dest),
        FilePermissions.fromOctalString("755")
      )
      layerConfigurationBuilder.build()
    }

    Jib
      .from(buildSettings.from)
      .setFileEntriesLayers(
        makeFileEntryLayerConfiguration(sourceAppPath, s"/$launcherApp")
      )
      .setCreationTime(Instant.now())
      .setEntrypoint(entrypoint: _*)
      .containerize(
        Containerizer.to(targetImage)
      )
  }

}
