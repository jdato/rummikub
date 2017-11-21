import game.{Game, Utils}
import model.{Player, Tile}

/**
  * Created by johannesdato on 10.11.17.
  */
class PlayRummikub {

}

object PlayRummikub extends App {

  println("Welcome to the Rummikub Scala Game!")
  var numberPlayers = 0
  val utils = new Utils()

  // Select number of players
  do {
    print("Number of players: ")
    // numberPlayers = 4
    numberPlayers = readInt()
  }
  while (numberPlayers < 2 || numberPlayers > 4)

  var game = new Game(numberPlayers)
  game.startGame()
}