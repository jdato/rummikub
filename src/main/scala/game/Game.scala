package game

import java.util.Random

import model.{Player, Playingfield, Rack, Tile}

/**
  * Created by johannesdato on 10.11.17.
  */
class Game(_numberOfPlayers: Int) {

  var playingfield : Playingfield = new Playingfield
  var numberOfPlayers: Int = _numberOfPlayers

  var pool : Set[Tile] = Set[Tile]()
  var players : Set[Player] = Set()
  var started = false
  var activePlayerId = 1

  def startGame(): Unit = {
    // calls init method
    initializePool()
    initializePlayers()

    players.head.rack.sortNumbers()
    players.head.rack.print()

    players.last.rack.sortColors()
    players.last.rack.print()
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
          pool.+=(tile)
          //tile.printTile()
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

    // Add the jokers TODO hier true oder flase übergeben ?
    val joker = new Tile(red, 0, true)
    pool.+=(joker)
    //joker.printTile()
    joker.color = black
    pool.+=(joker)
    //joker.printTile()
    println("Pool initialized.")
  }

  //
  def initializePlayers() = {
    for(i <- 1 to numberOfPlayers){
      var player = new Player(initializeRack(), i)
      //player.print()
      players.+=(player)
    }
  }
  def initializeRack() : Rack = {
    var tiles : Set[Tile] = Set()
    for(i <- 1 to 10){
      tiles.+=(getRandomTile())
    }
    return new Rack(tiles.toList)
  }

  def getRandomTile(): Tile = {
    //TODO num´tes Element aus dem pool holen, dort löschen und zurück geben
    // geht evtl einfacher, hab ich jedoch nirgends gefunden
    val num = new Random().nextInt(pool.size)
    var i = 0: Int
    for (t <- pool){
      if(i==num){
        pool.-=(t)
        return t
      }
      i=i+1
    }
    return null
  }

  // GIbt den gesammten Pool aus
  def printPool(): Unit = {
    for (t <- pool) t.printTile()
    //pool.foreach((t: Tile) => t.printTile())
  }
}

object Game {

}

