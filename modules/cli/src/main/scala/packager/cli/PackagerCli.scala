package packager.cli

import caseapp.core.app.{Command, CommandsEntryPoint}
import packager.cli.commands.Build

object PackagerCli extends CommandsEntryPoint {

  final override def defaultCommand: Option[Command[_]] = Some(Build)

  override def enableCompleteCommand    = true
  override def enableCompletionsCommand = true

  def commands: Seq[Command[_]] =
    Seq(Build)

  override def progName: String = "packager"
}
