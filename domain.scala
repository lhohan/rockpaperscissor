import java.util.logging.Handler
enum Hand:
  case Rock
  case Paper
  case Scissors

extension (hand: Hand) def show: String = hand.toString().toLowerCase()

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

enum Outcome:
  case PlayerWins
  case ComputerWins
  case Tie

import Outcome.*
extension (outcome: Outcome)
  def show: String = outcome match
    case PlayerWins   => "You win!"
    case Tie          => "Tie"
    case ComputerWins => "I win!"
