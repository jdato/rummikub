package view.tui

import akka.actor.{Actor, ActorRef}
import model.Messages._
import model.{Player, Playingfield, Tile}

class TuiView(val controller: ActorRef) extends Actor{

  controller ! RegisterObserver

  override def receive: Receive = {
    case s:String => println(s)
    case PrintMessage(message: String) => println(message)
    case PrintPlayingField(player: Player, playingfield: Playingfield) => printPlayingField(player, playingfield)
    case PrintTilesHorizontally(tiles : List[Tile]) => printTilesHorizontally(tiles)
    case PrintTile(tile: Tile) => printTile(tile)
    case AbortGame => context.system.terminate()
  }


  //prints the Playingfield for the specific Player
  def printPlayingField(player: Player, playingfield: Playingfield): Unit = {
    println("\n\n\n##########################################################################################")
    println("Played Tile Sets:")
    for (playedTileSet <- playingfield.playedTileSets) printTilesHorizontally(playedTileSet.tiles)
    println("##########################################################################################")
    println("Your Rack, Player " + player.id)
    player.rack.sortNumbers()
    player.rack.sortColors()
    printTilesHorizontally(player.rack.tiles)
    println("##########################################################################################")
    println("p: Pass move, c: Check moves, q:Quit Game")
  }

  def printTilesHorizontally(tiles : List[Tile]): Unit = {
    var space = ""

    tiles.foreach(t => printAndReset(t.colorCode, " --- "))
    println()
    tiles.foreach(t => {
      if(t.number < 10) space = " "
      else space = ""
      printAndReset(t.colorCode, "| " + t.number + space + "|")
    })
    println()
    tiles.foreach(t => printAndReset(t.colorCode, "| ☺ |"))
    println()
    tiles.foreach(t => printAndReset(t.colorCode, " --- "))
    println()
  }

  def printAndReset(color: String, string: String): String = {
    print(color + string + "\u001B[0m")
    color + string + "\u001B[0m"
  }

  def printTile(tile: Tile): Unit = {
    print(tile.colorCode + " --- \n")
    if(tile.number > 0) {
      if(tile.number < 10) print("| " + tile.number +" |\n")
      else print("| " + tile.number +"|\n")
    }
    if(tile.number == 0) print("| ☺ |\n")
    print("| ® |\n --- \n" + tile.reset)
  }

}
