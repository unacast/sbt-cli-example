import Console._

object Ansi {

  def green(msg: String) = withAnsiCode(GREEN, msg)
  def red(msg: String) = withAnsiCode(RED, msg)
  def blue(msg: String) = withAnsiCode(BLUE, msg)
  def magenta(msg: String) = withAnsiCode(MAGENTA, msg)
  def yellow(msg: String) = withAnsiCode(YELLOW, msg)
  def cyan(msg: String) = withAnsiCode(CYAN, msg)
  def bold(msg: String) = withAnsiCode(BOLD, msg)

  def withAnsiCode(in: String, msg: String) =
    s"$in$msg${Console.RESET}"

  val colorFunctions = Map(
    "green" → green _,
    "red" → red _,
    "blue" → blue _,
    "magenta" → magenta _,
    "yellow" → yellow _,
    "cyan" → cyan _,
    "bold" → bold _
  )

}
