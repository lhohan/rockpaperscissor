import cli.Console

@main
def startGame(): Unit =
  new RockPaperScissorsCLiGame(
    Console.cli
  ).start()
