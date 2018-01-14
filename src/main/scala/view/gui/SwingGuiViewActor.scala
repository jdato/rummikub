package view.gui

import akka.actor.{Actor, ActorSelection}
import model.Messages._
import model.{Player, Playingfield, Tile}

class SwingGuiViewActor extends Actor {

  val controller: ActorSelection = context.system.actorSelection("user/controller")
  controller ! RegisterObserver

  val frame = new SwingGuiFrame(controller)

  override def receive: Receive = {
    case Init => init()
    case StartGame => println("start swing gui game")
    case PrintMessage(message: String) => frame.statusPanel.setStatus(message)

    case PrintPlayingField(player: Player, playingfield: Playingfield) => printPlayingField(player, playingfield)
    case PrintTilesHorizontally(tiles : List[Tile]) => frame.statusPanel.setAlert("SwingGuiViewActor -> Receive -> PrintTilesHorizontally(tiles : List[Tile]) -> NIY")
    case PrintTile(tile: Tile) => frame.statusPanel.setAlert("SwingGuiViewActor -> Receive -> PrintTile(tile : Tile) -> NIY")
    case AbortGame => context.system.terminate()
  }

  def init(): Unit = {
    frame.statusPanel.setStatus("Welcome to the Scala Rummicup Game! To start the game, press the 'start' button.")
    // Implement start button and controller ! StartGame
  }

  def printPlayingField(player: Player, playingfield: Playingfield): Unit = {


    // 1. Print Played tilessets
    /*
      println("Played Tile Sets:")
      for (playedTileSet <- playingfield.playedTileSets) printTilesHorizontally(playedTileSet.tiles)
    */




    // 2. Print the Rack of the player
    /*
      println("Your Rack, Player " + player.id)
      player.rack.sortNumbers()
      player.rack.sortColors()
      printTilesHorizontally(player.rack.tiles)
     */



  }

  def printTilesHorizontally(tiles: List[Tile]): Unit = {

  }


}
