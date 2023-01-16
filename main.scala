//> using scala "3.2.1"

@main
def startGame() =
  new RockPaperScissorsCLiGame(
    Console.cmd
  )
    .start()
