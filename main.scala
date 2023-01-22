import cli.Console

@main
def startGame(): Unit =
  new RockPaperScissorsCLiGame(
    Console.cmd
  ).start()
