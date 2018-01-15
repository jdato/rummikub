package view.gui

import akka.actor.{Actor, ActorSelection}
import model.Messages._
import model.{Player, Playingfield, Tile, TileSet}

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
    case PrintPossibleTileSets(tileSets : List[TileSet]) => printPossibleTileSets(tileSets)
    case PrintTile(tile: Tile) => frame.statusPanel.setAlert("SwingGuiViewActor -> Receive -> PrintTile(tile : Tile) -> NIY")
    case AbortGame => context.system.terminate()
  }

  def init(): Unit = {
    frame.statusPanel.setStatus("Welcome to the Scala Rummicup Game! To start the game, press the 'start' button.")
  }

  def printPlayingField(player: Player, playingfield: Playingfield): Unit = {
    // 1. Print Played tilessets
    frame.gamePanel.printPlayingField(playingfield)

    // 2. Print the Rack of the player
    player.rack.sortNumbers()
    player.rack.sortColors()
    frame.gamePanel.printRack(player)

    // 3. Update frame to refresh view
    frame.visible_=(true)
    frame.gamePanel.possibleSetsPane.visible_=(false)
    frame.gamePanel.possibleSetsPane.visible_=(true)
  }

  def printPossibleTileSets(tileSets: List[TileSet]): Unit = {
    frame.gamePanel.printPossibleSets(tileSets)
    frame.visible_=(true)
  }
}
