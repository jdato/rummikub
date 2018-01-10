package controller

import java.util.Random

import model._

/**
  * Created by johannesdato on 17.11.17.
  */
class Utils {

  var pool: Set[Tile] = Set[Tile]()

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

  def pickRandomTile(): Tile = {
    val num = new Random().nextInt(pool.size)
    var i = 0: Int
    for (t <- pool) {
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

  def initializePool(): Unit = {
    var color = 0
    var twoDecks = 0
    var number = 0
    var numberCode = 0

    /*val red = "\u001B[31m"
    val blue = "\u001B[34m"
    val yellow = "\u001B[33m"
    val black = "\u001B[30m"*/

    for (color <- 1 to 4) {
      var currColor = matchColor(color)
      var currColorCode = matchColorCode(color)
      for (twoDecks <- 1 to 2) {
        for (number <- 1 to 13) {
          val tile = new Tile(currColorCode, currColor, number, false)
          pool.+=(tile)
        }
      }
    }
    println("Pool initialized.")
  }

  def matchColor(x: Int): String = x match {
    case 1 => "red"
    case 2 => "blue"
    case 3 => "yellow"
    case 4 => "black"
    case _ => "no color"
  }

  def matchColorCode(x: Int): String = x match {
    case 1 => "\u001B[31m"
    case 2 => "\u001B[34m"
    case 3 => "\u001B[33m"
    case 4 => "\u001B[30m"
    case _ => "no color"
  }


  //Initializes the rack and set random Tiles
  def initializeRack(): Rack = {
    var tiles: Set[Tile] = Set()
    for (i <- 1 to 14) {
      tiles.+=(pickRandomTileFromPool())
    }
    new Rack(tiles.toList) // Implicit return statement
  }

  //take a random Tile out of the pool
  def pickRandomTileFromPool(): Tile = {
    var poolArray = pool.toArray
    var tile = poolArray(new Random().nextInt(pool.size))
    pool.-=(tile)
    return tile
  }

  //checking for same colored series in the rack
  def checkSeries(player: Player): List[TileSet] = {
    //TODO method ignores jockers
    var rack = player.rack
    var tileSets: List[TileSet] = List[TileSet]()
    var tiles: List[Tile] = List[Tile]()

    var number: Int = 0
    var count: Int = 0
    var color: String = ""

    rack.sortNumbers()
    rack.sortColors()
    //set first color
    color = rack.tiles.head.colorCode
    rack.tiles.foreach(tile => {
      if (color == tile.colorCode) {
        if (tile.number == number + 1) {
          count.+=(1)
          number.+=(1)
          tiles = tiles.::(tile)
        }
        else if (tile.number == number) {
          //nothing to do
        }
        else {
          count = 1
          number = tile.number
          tiles = List[Tile]().::(tile)
        }
      } else {
        count = 1
        color = tile.colorCode
        tiles = List[Tile]().::(tile)
        number = tile.number
      }
      if (count >= 3) {
        if (player.madeFirstMove || checkSum(tiles)) {
          tileSets = tileSets.::(new TileSet(tiles, true))
        }
      }
    })
    return tileSets
  }

  // checking for any Sets in the rack
  def checkSet(player: Player): List[TileSet] = {
    //TODO method ignores jockers and same colored Tiles
    var rack = player.rack
    var tileSets: List[TileSet] = List[TileSet]()
    var tiles: List[Tile] = List[Tile]()
    var number: Int = 0
    var count: Int = 0
    var tilesToSet = List[Tile]()

    rack.sortNumbers()
    rack.tiles.foreach(tile => {
      if (number == tile.number) {
        count.+=(1)
        tiles = tiles.::(tile)
      } else {
        number = tile.number
        count = 1
        tiles = List[Tile]().::(tile)
      }
      if (count >= 3) {
        if (player.madeFirstMove || checkSum(tiles)) {
          tileSets = tileSets.::(new TileSet(tiles, false))
        }
      }
    })
    return tileSets
  }

  // check if player has 30 Points
  def checkSum(tiles: List[Tile]): Boolean = {
    return tiles.foldLeft(0) { (z, i) => z + i.number } >= 30
  }

  //check if Rack contains a street or a Set
  def checkMoves(player: Player): List[TileSet] = {
    var tileSets: List[TileSet] = List[TileSet]()
    tileSets = tileSets.:::(checkSeries(player))
    tileSets = tileSets.:::(checkSet(player))
    tileSets
  }

  //Initializes the players
  def initializePlayers(numberOfPlayers: Int): Set[Player] = {
    var players: Set[Player] = Set()
    for (i <- 1 to numberOfPlayers) {
      var player = new Player(initializeRack(), i)
      //player.print()
      players.+=(player)
    }
    return players
  }

  def selectStarterPosition(starter: Int, numberOfPlayers: Int): Int = starter match {
    case 1 => numberOfPlayers
    case 2 => 1
    case 3 => 2
    case 4 => 3
  }
}