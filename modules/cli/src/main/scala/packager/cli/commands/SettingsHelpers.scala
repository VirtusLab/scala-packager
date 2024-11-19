package packager.cli.commands

object SettingsHelpers {
  implicit class Mandatory[A](x: Option[A]) {
    def mandatory(error: String): A =
      x match {
        case Some(v) => v
        case None =>
          System.err.println(error)
          sys.exit(1)
      }
  }

  implicit class Validate[A](x: Option[A]) {
    def validate(cond: A => Boolean, error: String): Option[A] =
      x match {
        case Some(v) =>
          if (cond(v)) Some(v)
          else {
            System.err.println(error)
            sys.exit(1)
          }
        case None => None
      }
  }
}
