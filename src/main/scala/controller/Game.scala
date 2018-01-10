package controller

import model._

/**
  * Created by johannesdato on 10.11.17.
  */
class Game(_numberOfPlayers: Int, _utils: Utils) {
  var utils = _utils
  var playingfield: Playingfield = new Playingfield
  val numberOfPlayers: Int = _numberOfPlayers

  var game = new TextualGame(numberOfPlayers, utils)

  var players: Set[Player] = Set()
  var started = false
  var activePlayerId = 1

  def startGame(): Unit = {
    // calls init method
    utils.initializePool()
    players = utils.initializePlayers(numberOfPlayers)

    // Enters the game loop
    println("Game started.")
    started = true

    var firstRound = true
    var starter = game.gambleForStartingPositon(players, utils.pool)
    val positionBeforeStarter = utils.selectStarterPosition(starter.id, numberOfPlayers)

    while (started) {
      for (player <- players) {
        if (!firstRound) {
          player.pass = false
          player.rack.addTile(utils.pickRandomTile())
          game.printPlayingField(player, playingfield)
          started = game.play(player, playingfield, utils.checkMoves, players)
          if (!started) {
            abortGame(player)
            return
          }
        }
        else {
          if (player.id == positionBeforeStarter) firstRound = false
        }
      }
    }

  }

  def abortGame(p: Player): Unit = {
    // Exits the game loop
    println("Game aborted, by player " + p.id + "!")
    started = false
  }


}