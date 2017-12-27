package controller

import java.util.Random

import model.{Playingfield, Tile, TileSet}

/**
  * Created by johannesdato on 17.11.17.
  */
class Utils {

  // print tiles in row instead of in columns
  def printTilesHorizontally(tiles : List[Tile]): Unit = {
    var space = ""

    tiles.foreach(t => printAndReset(t.colorCode, " --- "))
    println()
    tiles.foreach(t => {
      if(t.number < 10) space = " "
      else space = ""
      printAndReset(t.colorCode, "| " + t.number + space + "|")
    })
    println()
    tiles.foreach(t => printAndReset(t.colorCode, "| ☺ |"))
    println()
    tiles.foreach(t => printAndReset(t.colorCode, " --- "))
    println()
  }

  def printTilesHorizontallyGrafic(tiles: List[Tile]): String = {
    var space = ""
    val string = StringBuilder.newBuilder
    string.append("\n")
    tiles.foreach(t => string.append(printAndReset(t.colorCode, " --- ")))
    println()
    string.append("\n")
    tiles.foreach(t => {
      if (t.number < 10) space = " "
      else space = ""
      string.append(printAndReset(t.colorCode, "| " + t.number + space + "|"))
    })
    println()
    string.append("\n")
    tiles.foreach(t => string.append(printAndReset(t.colorCode, "| ☺ |")))
    println()
    string.append("\n")
    tiles.foreach(t => string.append(printAndReset(t.colorCode, " --- ")))
    println()
    string.append("\n")
    return string.toString()
  }

  def printAndReset(color: String, string: String): String = {
    print(color + string + "\u001B[0m")
    color + string + "\u001B[0m"
  }

  def pickRandomTile(tiles: Set[Tile]): Tile = {
    val num = new Random().nextInt(tiles.size)
    var i = 0: Int
    for (t <- tiles) {
      if (i == num) {
        return t
      }
      i = i + 1
    }
    null
  }

  def checkAppend(tile: Tile, playingfield: Playingfield): TileSet = {
    for (tileSet <- playingfield.playedTileSets) {
      if (tileSet.series) {
        //check if tile can be added at the top or bottom
        if (tileSet.tiles.head.colorCode == tile.colorCode) {
          if (tileSet.tiles.head.number == tile.number - 1 || tileSet.tiles.last.number == tile.number + 1) {
            return tileSet
          }
        }
      } else {
        if (tileSet.tiles.head.number == tile.number) {
          return tileSet
        }
      }
    }
    return null
  }
}