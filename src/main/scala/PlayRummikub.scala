import game.Game

/**
  * Created by johannesdato on 10.11.17.
  */
class PlayRummikub {
}

object PlayRummikub extends App  {

    println("Welcome to the Rummikub Scala Game!")

    var numberPlayers = 0
    var a = 10
    // Game Loop
    //while(true) {

    // Select number of players
    do {
      print("Number of players: ")
      numberPlayers = readInt()
    }
    while(numberPlayers < 2 || numberPlayers > 4)

    var game = new Game(numberPlayers)

    game.startGame()
    //game.printPool()

    //} // End Game Loop
}