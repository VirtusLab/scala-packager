import sbt._

object Deps {
  def expecty       = "com.eed3si9n.expecty"       %% "expecty"       % "0.17.0"
  def munit         = "org.scalameta"              %% "munit"         % "1.2.0"
  def osLib         = "com.lihaoyi"                %% "os-lib"        % "0.11.5"
  def caseApp       = "com.github.alexarchambault" %% "case-app"      % "2.1.0"
  def thumbnailator = "net.coobird"                 % "thumbnailator" % "0.4.21"
  def image4j       = "org.jclarion"                % "image4j"       % "0.7"
  def jib           = "com.google.cloud.tools"      % "jib-core"      % "0.27.3"
  def commonsIo     = "commons-io"                  % "commons-io"    % "2.20.0"
}
