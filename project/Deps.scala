import sbt._

object Deps {
  def expecty = "com.eed3si9n.expecty" %% "expecty" % "0.15.1"
  def munit = "org.scalameta" %% "munit" % "0.7.29"
  def osLib = "com.lihaoyi" %% "os-lib" % "0.7.5"
  def caseApp = "com.github.alexarchambault" %% "case-app" % "2.1.0-M2"
  def thumbnailator = "net.coobird" % "thumbnailator" % "0.4.14"
  def image4j = "org.jclarion" % "image4j" % "0.7"
  def jib = "com.google.cloud.tools" % "jib-core" % "0.19.0"
  def commonsIo = "commons-io" % "commons-io" % "2.11.0"
}
