package game

import java.util.Random

import model._

/**
  * Created by johannesdato on 10.11.17.
  */
class Game(_numberOfPlayers: Int) {

  var playingfield: Playingfield = new Playingfield
  var numberOfPlayers: Int = _numberOfPlayers
  var utils: Utils = new Utils

  var pool: Set[Tile] = Set[Tile]()
  var players: Set[Player] = Set()
  var started = false
  var win = false

  def startGame(): Unit = {
    // calls init method
    initializePool()
    initializePlayers()

    // Enters the game loop
    println("Game started.")
    started = true

    var firstRound = true
    var starter = gambleForStartingPositon()

    while (started && !win) {
      for (player <- players) {
        if (!firstRound && started && !win) {
          player.pass = false
          player.rack.addTile(getRandomTile())
          printPlayingField(player)

          //while not passing its your turn
          while (!player.pass && !win && started) {
            //check if have won
            if (player.rack.tiles.size == 0) {
              win = true;
              println("Congratulations Player" + player.id + ", you have won!")
            } else {
              var input: String = readLine()
              input match {
                //case player doesn´t want to set any Tile
                case "p" =>
                  println("passed, next Player:")
                  player.pass = true

                //case player want to check for possible moves
                case "c" =>
                  checkMoves(player)

                //case player wants to exit game
                case "q" =>
                  abortGame(player)

                // invalid imput
                case _ => println("invalid Input")
              }
            }
          }
        }
        else {
          if(player.id == starter.id-1) firstRound = false
        }
      }
    }
  }

  def abortGame(p : Player): Unit = {
    // Exits the game loop
    println("Game aborted, by player " + p.id + "!")
    started = false
  }

  // Initializes the pool and sets all the available stones in it
  def initializePool(): Unit = {
    var color = 0
    var twoDecks = 0
    var number = 0

    val red = "\u001B[31m"
    val blue = "\u001B[34m"
    val yellow = "\u001B[33m"
    val black = "\u001B[30m"

    for (color <- 1 to 4) {
      var currColor = matchColor(color)
      for (twoDecks <- 1 to 2) {
        for (number <- 1 to 13) {
          val tile = new Tile(currColor, number, false)
          pool.+=(tile)
        }
      }
    }

    def matchColor(x: Int): String = x match {
      case 1 => red
      case 2 => blue
      case 3 => yellow
      case 4 => black
      case _ => "no color"
    }

    /* Joker funktionieren noch nicht
        // Add the jokers
        val joker = new Tile(red, 0, true)
        pool.+=(joker)
        joker.color = black
        pool.+=(joker)
    */
    println("Pool initialized.")
  }

  //Initializes the players
  def initializePlayers(): Unit = {
    for (i <- 1 to numberOfPlayers) {
      var player = new Player(initializeRack(), i)
      //player.print()
      players.+=(player)
    }
  }

  //Initializes the rack and set random Tiles
  def initializeRack(): Rack = {
    var tiles: Set[Tile] = Set()
    for (i <- 1 to 14) {
      tiles.+=(getRandomTile())
    }
    new Rack(tiles.toList) // Implicit return statement
  }

  def gambleForStartingPositon(): Player = {

    println("Gambling for the starting position.")

    var starter: List[Player] = List()
    var jokerPicked = false

    // Repeat if two player pick the same number or a joker has been picked
    do {
      starter = List()
      jokerPicked = false
      var initTiles: List[Tile] = List()
      // Initial picking of numbers
      players.foreach(p => {
        val tile = p.pickInitTile(getRandomTile())
        initTiles.::=(tile)
        println("Player " + p.id + " picked: ")
        tile.printTile()
      })
      val maxByVal = initTiles.maxBy(tile => tile.number)
      players.foreach(p => {
        if (p.initTile.number == maxByVal.number) {
          starter.::=(p)
        }
        if (p.initTile.number == 0) {
          jokerPicked = true
        }
      })
      if (jokerPicked) println("Joker picked. Repeating start.")
      if (starter.count(p => true) > 1) println("More than one highest tile. Repeating start.")
    } while (starter.count(p => true) > 1 || jokerPicked)

    val startPlayer = starter.head
    print("Player " + startPlayer.id + " starts.")
    startPlayer

  }

  //TODO handle no Tiles in pool
  //take a random Tile out of the pool
  def getRandomTile(): Tile = {
    var poolArray = pool.toArray
    var tile = poolArray(new Random().nextInt(pool.size))
    pool.-=(tile)
    return tile
  }

  //prints the Playingfield for the specific Player
  def printPlayingField(player: Player): Unit = {
    println("\n\n\n##########################################################################################")
    println("Played Tile Sets:")
    for (playedTileSet <- playingfield.playedTileSets) utils.printTilesHorizontally(playedTileSet.tiles)
    println("##########################################################################################")
    println("Your Rack, Player " + player.id)
    player.rack.sortNumbers()
    player.rack.sortColors()
    utils.printTilesHorizontally(player.rack.tiles)
    println("##########################################################################################")
    println("p: Pass move, c: Check moves, q:Quit Game")
  }

  def checkSum(tiles: List[Tile]): Boolean = {
    return tiles.foldLeft(0) { (z, i) => z + i.number } >= 30
  }

  def checkAppend(tile: Tile): TileSet = {
    for (tileSet <- playingfield.playedTileSets) {
      if (tileSet.series) {
        //check if tile can be added at the top or bottom
        if (tileSet.tiles.head.color == tile.color) {
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
    color = rack.tiles.head.color
    for (tile <- rack.tiles) {
      if (color == tile.color) {
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
        color = tile.color
        tiles = List[Tile]().::(tile)
        number = tile.number
      }
      if (count >= 3) {
        if (player.madeFirstMove || checkSum(tiles)) {
          tileSets = tileSets.::(new TileSet(tiles, true))
        }
      }
    }
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
    for (tile <- rack.tiles) {
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
    }
    return tileSets
  }

  //check the next move
  def checkMoves(player: Player): Unit = {
    var tileSetsToPlay: List[TileSet] = List[TileSet]()

    //check possible moves for the player

    var tilesToAppand: List[Tile] = List[Tile]()
    tileSetsToPlay = tileSetsToPlay.:::(checkSeries(player))
    tileSetsToPlay = tileSetsToPlay.:::(checkSet(player))
    var i = 1: Int

    if (player.madeFirstMove) {
      for (tile <- player.rack.tiles) {
        val tileSet = checkAppend(tile)
        if (tileSet != null) {
          tilesToAppand.::=(tile)
          println("press \"a" + i + "\" to append Tile:")
          tile.printTile()
          println("to the TileSet:")
          utils.printTilesHorizontally(tileSet.tiles)
          i = i + 1
        }
      }
    }
    i = 1

    if (tileSetsToPlay.size > 0 || tilesToAppand.size > 0) {
      for (tileSet <- tileSetsToPlay) {
        println("press \"s" + i + "\" to play Tileset:")
        utils.printTilesHorizontally(tileSet.tiles)
        i = i + 1
      }

      println("##########################################################################################")
      println("p: Pass move, s#: Play TileSet number #, a#: to append Tile # to a TileSet")
    } else {
      println("No possible moves found. Press \"p\" to pass move.")
    }

    //check player action

    var input = readLine()
    input match {
      //case player doesn´t want to set any Tile
      case "p" =>
        println("Player " + player.id + " passed, next Player:")
        player.pass = true
      case _ =>
        input.toList match {
          case 's' :: tileNumber :: Nil =>
            var number: Int = Integer.valueOf(tileNumber.toString)
            var i = 1: Int
            for (tileSet <- tileSetsToPlay) {
              if (i == (number)) {
                playingfield.playTileSet(tileSet)
                for (tile <- tileSet.tiles) {
                  player.rack.removeTile(tile)
                }
                player.madeFirstMove = true
              }
              i = i + 1
            }
            printPlayingField(player)
          case 'a' :: tileNumber :: Nil =>
            var number: Int = Integer.valueOf(tileNumber.toString)
            var i = 1: Int
            tilesToAppand = tilesToAppand.sortWith((x, y) => x.color < y.color)
            tilesToAppand = tilesToAppand.sortWith((x, y) => x.number < y.number)
            for (tile <- tilesToAppand) {
              if (i == (number)) {
                var tileSet = checkAppend(tile)
                tileSet.append(tile)
                player.rack.removeTile(tile)
              }
              i = i + 1
            }
            printPlayingField(player)
          case _ => println("False Input!!!")
        }
    }
  }
}

object Game {

}

