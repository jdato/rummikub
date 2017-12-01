package ui.tui

import game.{Game, Utils}

import scala.io.StdIn

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
      numberPlayers = StdIn.readInt()
  }
  while (numberPlayers < 2 || numberPlayers > 4)

  var game = new Game(numberPlayers)
  game.startGame()
}