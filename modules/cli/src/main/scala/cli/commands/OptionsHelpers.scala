package cli.commands

object OptionsHelpers {
  implicit class Mandatory[A](x: Option[A]) {
    def mandatory(error: String): A = {
      x match {
        case Some(v) => v
        case None =>
          System.err.println(error)
          sys.exit(1)
      }
    }
  }
}
