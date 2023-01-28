package core

import cli.Console
import cli.model.CliCommand
import cli.model.CliCommand.{Invalid, Play, Stop}
import core.model.Hand.{Paper, Rock, Scissors}
import core.model.{Hand, Outcome}
import core.model.Outcome.{ComputerWins, PlayerWins, Tie}

import scala.annotation.tailrec
import scala.util.Random

object model:
  enum Hand:
    case Rock
    case Paper
    case Scissors

  extension (hand: Hand) def show: String = hand.toString.toLowerCase()

  enum Outcome:
    case PlayerWins
    case ComputerWins
    case Tie

  extension (outcome: Outcome)
    def show: String = outcome match
      case PlayerWins   => "Player wins!"
      case Tie          => "Tie"
      case ComputerWins => "Computer wins!"

end model

trait Player:
  // a player provides (maybe) a hand when asked
  // - no hand means player indicates he wants to stop
  def nextHand(): Option[Hand]

object Player:
  def cliPlayer(console: Console): Player = new Player:

    @tailrec
    override def nextHand(): Option[Hand] =
      console.writeLine("Please enter your move: ")

      val line = console.readLine()
      CliCommand.parse(line) match
        case Play(hand) => Some(hand)
        case Stop       => None
        case Invalid =>
          console.writeLine("Invalid move!")
          nextHand()
    end nextHand
  end cliPlayer

  def computerPlayer(console: Console): Player = new Player:

    private var playCount = 0

    override def nextHand(): Option[Hand] =
      val plays = Hand.values.toVector
      val numberOfPlays = plays.length
      val maxPlays = 100

      if playCount < maxPlays then
        val randomIndex = Random.between(0, numberOfPlays)
        playCount = playCount + 1
        val hand = plays(randomIndex)
        console.writeLine(s"I play ${hand.show}")
        Some(hand)
      else None
    end nextHand
  end computerPlayer

end Player

object logic:
  def play(humanPlay: Hand, computerPlay: Hand): Outcome =
    (humanPlay, computerPlay) match
      case (Rock, Scissors)     => PlayerWins
      case (Scissors, Paper)    => PlayerWins
      case (Paper, Rock)        => PlayerWins
      case (Rock, Rock)         => Tie
      case (Scissors, Scissors) => Tie
      case (Paper, Paper)       => Tie
      case _                    => ComputerWins

end logic
