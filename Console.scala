import scala.io.StdIn
trait Console:
  def readLine(): String
  def writeLine(line: String): Unit

object Console:
  def cmd: Console = new Console {
    def readLine(): String = StdIn.readLine()
    def writeLine(line: String): Unit = println(line)
  }
