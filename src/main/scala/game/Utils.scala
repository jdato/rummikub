package game

import model.Tile

/**
  * Created by johannesdato on 17.11.17.
  */
class Utils {

  // print tiles in row instead of in columns
  def printTilesHorizontally(tiles : List[Tile]): Unit = {
    var space = ""

    tiles.foreach(t => printAndReset(t.color, " --- "))
    println()
    tiles.foreach(t => {
      if(t.number < 10) space = " "
      else space = ""
      printAndReset(t.color, "| " + t.number + space + "|")
    })
    println()
    tiles.foreach(t => printAndReset(t.color, "| ☺ |"))
    println()
    tiles.foreach(t => printAndReset(t.color, " --- "))
    println()

  }

  def printAndReset(color: String, string: String): Unit = {
    print(color + string + "\u001B[0m")
  }
}

object Utils {

}
