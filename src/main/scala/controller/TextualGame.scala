package controller

import java.util.Random

import model.{Player, Playingfield, Tile, TileSet}

import scala.io.StdIn

class TextualGame extends GameTrait {

  val utils: Utils = new Utils

  override def play(player: Player, playingfield: Playingfield, checkMoves: Player => List[TileSet]): Boolean = {
    var abort = false
    //while not passing its your turn
    while (!player.pass) {
      var input: String = StdIn.readLine()

      input match {
        //case player doesn´t want to set any Tile
        case "p" =>
          println("passed, next Player:")
          player.pass = true
        //case player want to check for possible moves
        case "c" =>
          playMove(checkMoves(player), player, playingfield)
        //case player wants to exit game
        case "q" =>
          abort = true
          return false
        // invalid imput
        case _ => println("invalid Input")
      }
    }
    if(abort) false
    else true
  }

  override def playMove(possibleMoves: List[TileSet], player: Player, playingfield: Playingfield): Unit = {

    if (possibleMoves.nonEmpty) {
      var i = 0: Int
      for (tileSet <- possibleMoves) {
        i = i + 1
        println("press \"s" + i + "\" to play Tileset:")
        utils.printTilesHorizontally(tileSet.tiles)
      }
      println("##########################################################################################")
      println("p: Pass move, s#: Play TileSet number #")
    } else {
      println("No possible moves detected! Press \"p\" to pass move.")
    }

    var input = StdIn.readLine()
    input match {
      //case player doesn´t want to set any Tile
      case "p" =>
        println("Player " + player.id + " passed, next Player:")
        player.pass = true
      case _ =>
        input.toList match {
          case 's' :: tileNumber :: Nil =>
            var number: Int = Integer.valueOf(tileNumber.toString)
            var i = 0: Int
            for (tileSet <- possibleMoves) {
              if (i == (number - 1)) {
                playingfield.playTileSet(tileSet)
                for (tile <- tileSet.tiles) {
                  player.rack.removeTile(tile)
                }
              }
              i = i + 1
            }
            printPlayingField(player, playingfield)
          case _ => println("False Input!!!")
        }
    }
  }

  //prints the Playingfield for the specific Player
  override def printPlayingField(player: Player, playingfield: Playingfield): Unit = {
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

  def gambleForStartingPositon(players: Set[Player], pool: Set[Tile]): Player = {

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
        val tile = p.pickInitTile(pickRandomTile(pool))
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
    startPlayer
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
}
