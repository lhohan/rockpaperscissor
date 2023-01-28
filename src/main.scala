import cli.Console
import scala.collection.immutable.LazyList.cons
import cli.model.CliCommand.*
import cli.model.CliCommand
import core.model.Hand.*
import core.model.Outcome.*

import scala.annotation.tailrec
import scala.util.Random
import cli.Console
import core.model.{Hand, Outcome}
import core.logic.play
import core.Player
import core.Player.*

@main
def startGame(): Unit =
  val console = Console.cli
  val game = RockPaperScissorsGame.singlePlayer(console)
  // For fun:
  // Needs refactoring to print out non-hardcoded names.
  //  val game = RockPaperScissorsGame.twoPlayer(console)
  //  val game = RockPaperScissorsGame.twoComputer(console)
  game.start()

class RockPaperScissorsGame private (
    console: Console,
    player1: Player,
    player2: Player
):
  import RockPaperScissorsGame.*

  def start(): Unit =
    writeWelcome(console)
    val outcomes =
      playGame(console, player1, player2)
    writeGameSummary(outcomes, console)
  end start

end RockPaperScissorsGame

object RockPaperScissorsGame:

  /** A default Rock Paper Scissors game takes place between a player on CLI and
    * a computer.
    */
  def singlePlayer(console: Console): RockPaperScissorsGame =
    val player1 = cliPlayer(console)
    val player2 = computerPlayer(console)
    new RockPaperScissorsGame(console, player1, player2)

  def twoPlayer(console: Console): RockPaperScissorsGame =
    val player1 = cliPlayer(console)
    val player2 = cliPlayer(console)
    new RockPaperScissorsGame(console, player1, player2)

  def twoComputer(console: Console): RockPaperScissorsGame =
    val player1 = computerPlayer(console)
    val player2 = computerPlayer(console)
    new RockPaperScissorsGame(console, player1, player2)

  /** Coordinate getting the next hands of ech player until a player indicates
    * they want to stop.
    *
    * Getting hands happens _sequentially_. First the hand of player1 is
    * request, then the hand of player2.
    *
    * Each outcome gets written to the console.
    *
    * @return
    *   All outcomes of rounds played.
    */
  private def playGame(
      console: Console,
      player1: Player,
      player2: Player
  ): List[Outcome] =
    case class Players(player1: Player, player2: Player)

    val outcomes = LazyList
      .unfold(Players(player1, player2)) { players =>
        for {
          hand1 <- players.player1.nextHand()
          hand2 <- players.player2.nextHand()
          outcomeAndPlayers <-
            val outcome = play(hand1, hand2)
            console.writeLine(outcome.show)
            Some(outcome, players)
        } yield outcomeAndPlayers
      }
      .toList
    end outcomes

    outcomes
  end playGame

  private def writeWelcome(console: Console): Unit =
    console.writeLine("Welcome to Rock Paper Scissors!")
    console.writeLine(
      "Valid commands are: 'rock', 'paper', 'scissors', 'stop'."
    )
    console.writeLine("")

  private def writeGameSummary(
      outcomes: List[Outcome],
      console: Console
  ): Unit =
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

end RockPaperScissorsGame
