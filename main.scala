//> using scala "3.2.1"

import cli.Console

@main
def startGame(): Unit =
  new RockPaperScissorsCLiGame(
    Console.cmd
  ).start()
