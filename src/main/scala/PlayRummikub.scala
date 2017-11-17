import game.{Game, Utils}
import model.{Player, Tile}

/**
  * Created by johannesdato on 10.11.17.
  */
class PlayRummikub {

  def playerpickTile(): Unit = {

  }

}

object PlayRummikub extends App {

  println("Welcome to the Rummikub Scala Game!")
  var numberPlayers = 0
  val utils = new Utils()

  // Select number of players
  do {
    print("Number of players: ")
    numberPlayers = 4
    //numberPlayers = readInt()
  }
  while (numberPlayers < 2 || numberPlayers > 4)

  var game = new Game(numberPlayers)
  game.startGame()

  println("Gambling for the starting position.")

  var starter: List[Player] = List()
  var jokerPicked = false

  // Repeat if two player pick the same number
  do {
    starter = List()
    jokerPicked = false
    var initTiles: List[Tile] = List()
    // Initial picking of numbers
    game.players.foreach(p => {
      val tile = p.pickInitTile(game.getRandomTile())
      initTiles.::=(tile)
      println("Player " + p.id + " picked: ")
      tile.printTile()
    })
    val maxByVal = initTiles.maxBy(tile => tile.number)
    game.players.foreach(p => {
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

  print("Player " + starter.head.id + " starts.")



  /*
  println("Players Rack: ")
  game.players.head.rack.sortNumbers()
  game.players.head.rack.sortColors()
  game.players.head.rack.printRack()
  */

}