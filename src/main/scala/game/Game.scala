package game

import java.util.Random

import model.{Player, Playingfield, Rack, Tile}

/**
  * Created by johannesdato on 10.11.17.
  */
class Game(_numberOfPlayers: Int) {

  var playingfield: Playingfield = new Playingfield
  var numberOfPlayers: Int = _numberOfPlayers

  var pool: Set[Tile] = Set[Tile]()
  var players: Set[Player] = Set()
  var started = false
  var activePlayerId = 1

  def startGame(): Unit = {
    // calls init method
    initializePool()
    initializePlayers()

    // Enters the game loop
    println("Game started.")
  }

  def abortGame(): Unit = {
    // Exits the game loop
    println("Implement abort game method.")
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

    // Add the jokers
    val joker = new Tile(red, 0, true)
    pool.+=(joker)
    joker.color = black
    pool.+=(joker)

    println("Pool initialized.")
  }

  //
  def initializePlayers(): Unit = {
    for (i <- 1 to numberOfPlayers) {
      var player = new Player(initializeRack(), i)
      //player.print()
      players.+=(player)
    }
  }

  def initializeRack(): Rack = {
    var tiles: Set[Tile] = Set()
    for (i <- 1 to 14) {
      tiles.+=(getRandomTile())
    }
    new Rack(tiles.toList) // Implicit return statement
  }

  def getRandomTile(): Tile = {
    //TODO get Random Tile from pool
    val num = new Random().nextInt(pool.size)
    var i = 0: Int
    for (t <- pool) {
      if (i == num) {
        pool.-=(t)
        return t
      }
      i = i + 1
    }
    null // Implicit return statement
  }

  // GIbt den gesammten Pool aus
  def printPool(): Unit = {
    for (t <- pool) t.printTile()
    //pool.foreach((t: Tile) => t.printTile())
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
}

object Game {

}

