import Ansi._
import sbt.complete.DefaultParsers._
import sbt.complete.Parser

// A ADT representing the different commands this program understands
// The run() method is call whenever a command is encountered in the main loop
// in the Cli trait
sealed trait CliCommand {
  def run(): Unit
}

case class Colorize(chosenColor: String, inputString: String) extends CliCommand {
  override def run(): Unit =
    println(
      Ansi.colorFunctions.getOrElse(
        chosenColor,
        (_: String) ⇒ s"I don't know how to make your text $chosenColor"
      )(inputString)
    )
}

object Exit extends CliCommand {
  override def run(): Unit = println("Bye!")
}

object SayHi extends CliCommand {
  override def run(): Unit = println(green("Hi you, the Rock Steady Crew!"))
}

object Help extends CliCommand {
  override def run(): Unit =
    println(
      s"""
         |Usage: hi
         |  Say hi!
         |
         |Usage: colorize [color] text
         |  Colorizes the text with the chosen color
         |
         |Usage: help
         |  Display this help
         |
         |Usage: exit
         |  Exit the CLI
         |
         |""".stripMargin
    )
}

object CliCommandParser {

  def parser: Parser[CliCommand] = {
    val hi        = token("hi"    ^^^ SayHi)
    val help      = token("help"  ^^^ Help)
    val exit      = token("exit"  ^^^ Exit)

    val colorize  = {

      // A ~> matches but discards the part to it's left,
      // in this case the colorize command
      val command = token("colorize") ~> Space

      // Here the list of available colors is reduced into one combined parser
      val colorChooser =
        Ansi.colorFunctions.keys.toList
        .map(t ⇒ token(t) <~ Space).reduce(_ | _)

      // This parses a potentially quoted String value, aka the users input
      val inputText = token(StringBasic, "[The text to colorize]")

      // The parsers gets combined and using the ~ we fetch both
      // the chosen color and the input text as a tuple
      val combinedParser = command ~> colorChooser ~ inputText

      // The map applies a function to the result of the combinded parser,
      // in this case we create a Colorize command
      combinedParser.map { case (choseColor, text) ⇒ Colorize(choseColor, text) }
    }

    // The main parser is constructed by combining these four parsers representing
    // the different known commands
    colorize | hi | help | exit
  }

}