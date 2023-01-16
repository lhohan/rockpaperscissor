import Hand.*
import Outcome.*

class RockPaperScissorsRulesSuite extends munit.FunSuite {

  test(
    "Rock should beat Scissors (and Scissors loses to Rock)"
  ) {
    // dd: explicitly name parameters to aid understanding of test
    assertPlayer1Wins(humanPlay = Hand.Rock, computerPlay = Hand.Scissors)
    assertPlayer2Wins(humanPlay = Hand.Scissors, computerPlay = Hand.Rock)
  }

  test(
    "Scissors should beat Paper (and Paper loses to Scissors)"
  ) {
    // dd: explicitly name parameters to aid understanding of test
    assertPlayer1Wins(humanPlay = Hand.Scissors, computerPlay = Hand.Paper)
    assertPlayer2Wins(humanPlay = Hand.Paper, computerPlay = Hand.Scissors)
  }

  test(
    "Paper should beat Rock (and Rock loses to Paper)"
  ) {
    // dd: explicitly name parameters to aid understanding of test
    assertPlayer1Wins(humanPlay = Hand.Paper, computerPlay = Hand.Rock)
    assertPlayer2Wins(humanPlay = Hand.Rock, computerPlay = Hand.Paper)
  }

  test("Same play by human and computer should be a draw") {
    val allPlays = Hand.values
    allPlays.foreach { play =>
      assertDraw(play, play)
    }
  }

  private def assertDraw(humanPlay: Hand, computerPlay: Hand): Unit = {
    val obtained = RockPaperScissorsCLiGame.play(humanPlay, computerPlay)
    val expected = Outcome.Tie
    assertEquals(
      obtained,
      expected,
      s"Same play for human ($humanPlay) and computer ($computerPlay) should result in a draw"
    )
  }

  private def assertPlayer1Wins(humanPlay: Hand, computerPlay: Hand): Unit = {
    val obtained = RockPaperScissorsCLiGame.play(humanPlay, computerPlay)
    val expected = Outcome.PlayerWins
    assertEquals(obtained, expected)
  }

  private def assertPlayer2Wins(humanPlay: Hand, computerPlay: Hand): Unit = {
    val obtained = RockPaperScissorsCLiGame.play(humanPlay, computerPlay)
    val expected = Outcome.ComputerWins
    assertEquals(obtained, expected)
  }
}
