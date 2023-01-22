package cli

import core.model

import scala.io.StdIn
trait Console:
  def readLine(): String
  def writeLine(line: String): Unit

end Console

object Console:
  def cli: Console = new Console {
    def readLine(): String = StdIn.readLine()
    def writeLine(line: String): Unit = println(line)
  }

end Console

object model:

  import core.model.Hand

  case object Invalid

  enum CliCommand:
    case Stop
    case Play(h: Hand)
    case Invalid

  object CliCommand:

    import Hand.*

    def parse(s: String): CliCommand =
      s.toLowerCase match
        case "rock"     => Play(Rock)
        case "paper"    => Play(Paper)
        case "scissors" => Play(Scissors)
        case "stop"     => Stop
        case _          => Invalid

  end CliCommand

end model
