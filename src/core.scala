package core

import cli.Console
import cli.model.CliCommand
import cli.model.CliCommand.{Invalid, Play, Stop}
import core.model.Hand.{Paper, Rock, Scissors}
import core.model.{Hand, Outcome}
import core.model.Outcome.{Player1Wins, Player2Wins, Tie}

import scala.annotation.tailrec
import scala.util.Random

object model:
  enum Hand:
    case Rock
    case Paper
    case Scissors

  extension (hand: Hand) def show: String = hand.toString.toLowerCase()

  enum Outcome:
    case Player1Wins
    case Player2Wins
    case Tie

  extension (outcome: Outcome)
    def show(playerName1: String, playerName2: String): String = outcome match
      case Player1Wins => s"$playerName1 wins!"
      case Tie         => "Tie"
      case Player2Wins => s"$playerName2 wins!"

end model

trait Player:
  // a player provides (maybe) a hand when asked
  // - no hand means player indicates he wants to stop
  def nextHand(): Option[Hand]

  def name: String

object Player:
  def cliPlayer(console: Console, playerName: String = "Player"): Player =
    new Player:

      @tailrec
      override def nextHand(): Option[Hand] =
        console.writeLine(s"Please enter your move $name: ")

        val line = console.readLine()
        CliCommand.parse(line) match
          case Play(hand) => Some(hand)
          case Stop       => None
          case Invalid =>
            console.writeLine("Invalid move!")
            nextHand()
      end nextHand

      override val name: String = playerName
  end cliPlayer

  def computerPlayer(
      console: Console,
      playerName: String = "Computer"
  ): Player = new Player:

    private var playCount = 0

    override def nextHand(): Option[Hand] =
      val plays = Hand.values.toVector
      val numberOfPlays = plays.length
      val maxPlays = 100

      if playCount < maxPlays then
        val randomIndex = Random.between(0, numberOfPlays)
        playCount = playCount + 1
        val hand = plays(randomIndex)
        console.writeLine(s"$name plays ${hand.show}")
        Some(hand)
      else None
    end nextHand

    override val name: String = playerName
  end computerPlayer

end Player

object logic:
  def play(player1Hand: Hand, player2Hand: Hand): Outcome =
    (player1Hand, player2Hand) match
      case (Rock, Scissors)     => Player1Wins
      case (Scissors, Paper)    => Player1Wins
      case (Paper, Rock)        => Player1Wins
      case (Rock, Rock)         => Tie
      case (Scissors, Scissors) => Tie
      case (Paper, Paper)       => Tie
      case _                    => Player2Wins

end logic
