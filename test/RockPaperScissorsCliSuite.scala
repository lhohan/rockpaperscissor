import munit.Assertions.fail

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Queue
import scala.util.Try

import cli.Console

class RockPaperScissorsCliSuite extends munit.FunSuite {

  import RockPaperScissorsCliSuite.*

  // End to end test closest to actually running the application testing the overall behavior.
  test("should print player outcomes if player plays some valid moves") {

    // Setup a mock console with pre-set test commands as inputs
    // The mock console records the outputs which can be verified
    val inputs = mutable.Queue("rock", "fire", "paper", "scissors", "stop")
    val outputs = ListBuffer.empty[String]
    val consoleMock = new ConsoleMock(inputs, outputs)

    // run
    RockPaperScissorsGame.singlePlayer(consoleMock).start()

    // verify
    val expectedOutputs: ListBuffer[String => Unit] = ListBuffer(
      expect("Welcome to Rock Paper Scissors!"),
      expect("Valid commands are: 'rock', 'paper', 'scissors', 'stop'."),
      expect(""),
      expect("Please enter your move Player: "), // rock
      expectComputerMove,
      expectGameOutCome,
      expect("Please enter your move Player: "), // fire
      expectInvalid,
      expect("Please enter your move Player: "), // paper
      expectComputerMove,
      expectGameOutCome,
      expect("Please enter your move Player: "), // scissors
      expectComputerMove,
      expectGameOutCome,
      expect("Please enter your move Player: "), // stop
      expectGameEnded,
      expectNoMoreMessages
    )

    val outputsWithExpected = outputs.zip(expectedOutputs)
    val outputsWithExpectedWithIndex = outputsWithExpected.zipWithIndex

    outputsWithExpectedWithIndex.foreach { (outputWithExpectedElement, index) =>
      val (obtained, assertion) = outputWithExpectedElement
      try { assertion(obtained) }
      catch {
        case e: munit.FailException =>
          println(s"Failed at (zero-based) index: $index")
          throw e
      }
    }
  }

}

object RockPaperScissorsCliSuite:

  val expectGameOutCome: String => Unit = line =>
    line match
      case "Player wins!"   => ()
      case "Computer wins!" => ()
      case "Tie"            => ()
      case _                => fail(s"'$line' is not a valid outcome")

  val expectInvalid: String => Unit = line =>
    assert(line.toLowerCase.contains("invalid"), s"'$line' should be invalid")

  val expectComputerMove: String => Unit = line =>
    assert(
      line.toLowerCase.contains("computer play"),
      s"'$line' should be computer move"
    )

  def expect(expected: String): String => Unit = line =>
    assert(line == expected, s"''$line' does not match expected: $expected")

  val expectGameEnded: String => Unit = line =>
    assert(
      line.toLowerCase.contains("game ended!"),
      s"'$line' should contain 'game ended!'"
    )

  val expectNoMoreMessages: String => Unit = receivedMsg =>
    fail(s"No more output expected. Got: $receivedMsg")

  class ConsoleMock(inputs: mutable.Queue[String], outputs: ListBuffer[String])
      extends Console:

    // change this to 'true' if you want to see actual output while e.g. debugging
    private val printToConsole = false

    def readLine(): String =
      Try(inputs.dequeue())
        .map(input =>
          if (printToConsole) println(s"debug: input: $input")
          input
        )
        .getOrElse("stop")

    def writeLine(line: String): Unit =
      if (printToConsole) println(s"debug: output: $line")
      outputs.append(line)
  end ConsoleMock

end RockPaperScissorsCliSuite
