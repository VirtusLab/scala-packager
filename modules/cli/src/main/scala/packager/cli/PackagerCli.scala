package packager.cli

import caseapp.core.app.CommandsEntryPoint
import packager.cli.commands.Build

object PackagerCli extends CommandsEntryPoint {

  final override def defaultCommand = Some(Build)

  override def enableCompleteCommand    = true
  override def enableCompletionsCommand = true

  val commands = Seq(
    Build
  )

  override def progName: String = "packager"
}
