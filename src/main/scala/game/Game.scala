package game

import model.{Player, Playingfield, Tile}

/**
  * Created by johannesdato on 10.11.17.
  */
class Game(numberOfPlayers: Int) {

  var playingfield : Playingfield = new Playingfield
  var players: Int = numberOfPlayers

  var pool : Set[Tile] = Set()
  var started = false
  var activePlayerId = 1

  def startGame(): Unit = {
    // calls init method
    initializePool()



    // Enters the game loop
    println("Implement start game method.")
  }

  def abortGame(): Unit = {
    // Exits the game loop
    println("Implement abort game method.")
  }

  def initializePool(): Unit = {
    // Initializes the pool and sets all the available stones in it
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
          pool.+(tile)
          tile.printTile()
        }
      }
    }

    def matchColor(x: Int) : String = x match {
      case 1 => red
      case 2 => blue
      case 3 => yellow
      case 4 => black
      case _ => "no color"
    }

    // Add the jokers
    val joker = new Tile(red, 0, false)
    pool.+(joker)
    joker.printTile()
    joker.color = black
    pool.+(joker)
    joker.printTile()

    println("Pool initialized.")
  }

  // TODO: Doesn't work & Remove
  def printPool(): Unit = {
    pool.foreach((t: Tile) => t.printTile())
  }
}

object Game {

}

