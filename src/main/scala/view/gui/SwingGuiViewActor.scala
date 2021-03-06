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
    case PrintPossibleTileSets(tileSets : List[TileSet]) => printPossibleTileSets(tileSets)
    case PrintPossibleAppendsToTileSets(tilesToAppendToTileSet : Map[Tile, TileSet]) => printPossibleAppendToTileSets(tilesToAppendToTileSet)
    case GameOver(player : Player) => endGame(player)
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

    frame.gamePanel.possibleSetsPane.contents.clear()
    frame.gamePanel.possibleAppendsPane.contents.clear()

    frame.gamePanel.possibleSetsPane.visible_=(false)
    frame.gamePanel.possibleSetsPane.visible_=(true)
    frame.gamePanel.possibleAppendsPane.visible_=(false)
    frame.gamePanel.possibleAppendsPane.visible_=(true)
  }

  def printPossibleTileSets(tileSets: List[TileSet]): Unit = {
    frame.gamePanel.printPossibleSets(tileSets)
    frame.visible_=(true)
  }

  def printPossibleAppendToTileSets(tileToSet: Map[Tile, TileSet]): Unit = {
    frame.gamePanel.printPossibleAppends(tileToSet)
    frame.visible_=(true)
  }

  def endGame(player: Player): Unit = {
    frame.statusPanel.setAlert("Congratulations Player " + player.id + "!! You've won the game!!")

    frame.gamePanel.possibleSetsPane.contents.clear()
    frame.gamePanel.possibleAppendsPane.contents.clear()
    frame.gamePanel.rackPane.contents.clear()

    frame.gamePanel.possibleSetsPane.visible_=(false)
    frame.gamePanel.possibleSetsPane.visible_=(true)
    frame.gamePanel.possibleAppendsPane.visible_=(false)
    frame.gamePanel.possibleAppendsPane.visible_=(true)
    frame.gamePanel.rackPane.visible_=(false)
    frame.gamePanel.rackPane.visible_=(true)
  }
}
