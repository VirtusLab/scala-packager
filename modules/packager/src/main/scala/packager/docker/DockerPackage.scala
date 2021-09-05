package packager.docker

import com.google.cloud.tools.jib.api.{
  Containerizer,
  DockerDaemonImage,
  ImageReference,
  Jib
}
import com.google.cloud.tools.jib.api.buildplan.AbsoluteUnixPath
import packager.Packager
import packager.config.DockerSettings

import java.time.Instant
import java.util.Arrays

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

    Jib
      .from(buildSettings.from)
      .addLayer(
        Arrays.asList(sourceAppPath.toNIO),
        AbsoluteUnixPath.get("/")
      )
      .setCreationTime(Instant.now())
      .setEntrypoint(buildSettings.exec, s"/$launcherApp")
      .containerize(
        Containerizer.to(targetImage)
      )
  }

}
