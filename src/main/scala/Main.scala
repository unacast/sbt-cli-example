import java.io.File
import Ansi._
import sbt.complete.Parser
import scala.util.Try

object Main extends App with Cli {
  runCli()
}

trait Cli {

  val parser = CliCommandParser.parser

  // Starts the CLI and runs in a loop parsing commands until it encounters the
  // Exit command. The Help command is passed as the initial command.
  def runCli() = {
    def loop(initialCmd: Option[CliCommand] = None): Unit =
      Try(initialCmd orElse readLine(parser) match {
        case Some(Exit) ⇒ Exit.run()
        case None       ⇒ loop()
        case Some(cmd)  ⇒ cmd.run(); loop()
      }).recover { case _ ⇒
        println(red("Ooops! Something went wrong, let's try again \n")); loop()
      }

    loop(Some(Help))
  }

  // Uses sbt JLine reader to read input from the user
  private def readLine[U](parser: Parser[U]): Option[U] = {
    val reader = new sbt.FullReader(Some(new File("/tmp/clihistory")), parser)
    reader.readLine(prompt = "> ") flatMap { line ⇒
      Parser.parse(line, parser).fold(_ ⇒ None, Some(_))
    }
  }
}