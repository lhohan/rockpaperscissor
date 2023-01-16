import scala.collection.immutable.LazyList.cons
import CliCommand.*
import Hand.*
import Outcome.*
import scala.annotation.tailrec
import scala.util.Random

class RockPaperScissorsCLiGame(console: Console):
  import RockPaperScissorsCLiGame.*

  // a player provides (maybe) a hand when asked
  // no hand means player indicates he wants to stop
  type Player =
    () => Option[
      Hand
    ] // could be trait with more clear method (vs more concise)
  case class Players(player1: Player, player2: Player)

  def start(): Unit =
    writeWelcome(console)

    val outcomes = LazyList
      .unfold(Players(cliPlayer, computerPlayer)) { players =>
        for {
          hand1 <- players.player1.apply()
          hand2 <- players.player2.apply()
          _ = console.writeLine(s"I play ${hand2.show}")
          outcomeAndPlayers <-
            val outcome = RockPaperScissorsCLiGame.play(hand1, hand2)
            console.writeLine(outcome.show)
            Some(outcome, players)
        } yield outcomeAndPlayers
      }
      .toList
    end outcomes

    writeGameSummary(outcomes, console)
  end start

  @tailrec
  private def cliPlayer(): Option[Hand] =
    console.writeLine("Please enter your move: ")
    val line = console.readLine()
    CliCommand.parse(line) match
      case Play(hand) => Some(hand)
      case Stop       => None
      case Invalid =>
        console.writeLine("Invalid move!")
        cliPlayer()

  val computerPlayer: () => Option[Hand] = () =>

    val plays = Hand.values.toVector
    val numberOfPlays = plays.length
    val maxPlays = 100
    var playCount = 0

    if (playCount < maxPlays) then
      val randomIndex = Random.between(0, numberOfPlays)
      playCount = playCount + 1
      val hand = plays(randomIndex)
      Some(hand)
    else None

end RockPaperScissorsCLiGame

object RockPaperScissorsCLiGame:

  def writeWelcome(console: Console): Unit =
    console.writeLine("Welcome to Rock Paper Scissors!")
    console.writeLine("")

  def writeGameSummary(outcomes: List[Outcome], console: Console): Unit =
    case class Stats(playerWins: Int = 0, computerWins: Int = 0, ties: Int = 0)

    val stats = outcomes.foldLeft(Stats()) { (stats, outcome) =>
      outcome match
        case PlayerWins   => stats.copy(playerWins = stats.playerWins + 1)
        case ComputerWins => stats.copy(computerWins = stats.computerWins + 1)
        case Tie          => stats.copy(ties = stats.ties + 1)
    }

    val msg = s"""
                 |Game ended! Here are the results:
                 |    Player wins  : ${stats.playerWins}
                 |    Computer wins: ${stats.computerWins}
                 |    Ties         : ${stats.ties}
                 |""".stripMargin
    console.writeLine(msg)
    ()
  end writeGameSummary

  // todo: move to non-cli object
  def play(humanPlay: Hand, computerPlay: Hand): Outcome =
    (humanPlay, computerPlay) match
      case (Rock, Scissors)     => PlayerWins
      case (Scissors, Paper)    => PlayerWins
      case (Paper, Rock)        => PlayerWins
      case (Rock, Rock)         => Tie
      case (Scissors, Scissors) => Tie
      case (Paper, Paper)       => Tie
      case _                    => ComputerWins

end RockPaperScissorsCLiGame
