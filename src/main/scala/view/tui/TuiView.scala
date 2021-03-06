package view.tui

import akka.actor.{Actor, ActorSelection}
import model.Messages._
import model.{Player, Playingfield, Tile}

class TuiView extends Actor{

  val controller: ActorSelection = context.system.actorSelection("user/controller")
  controller ! RegisterObserver

  override def receive: Receive = {
    case s:String => println(s)
    case Init => init()
    case PrintMessage(message: String) => println(message)
    case PrintPlayingField(player: Player, playingfield: Playingfield) => printPlayingField(player, playingfield)
    case PrintTilesHorizontally(tiles : List[Tile]) => printTilesHorizontally(tiles)
    case PrintTile(tile: Tile) => printTile(tile)
    case GameOver(player : Player) => endGame(player)
    case AbortGame => context.system.terminate()
    case Input(input: String) => processInput(input)
  }

  def processInput(input: String): Unit = {
    input match {
      case "start" => controller ! StartGame
      case "p" => controller ! Pass
      case "c" => controller ! Check
      case "q" => controller ! Quit
      case i if i.startsWith("s") || i.startsWith("a") => controller ! SetTiles(i)
      case _ => controller ! InvalidInput
    }
  }

  def init(): Unit = {
    println("###############################\n#        SCALA RUMMIKUB       #\n###############################")
    println("To start the game write 'start' enter!")
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

  def endGame(player: Player): Unit = {
    println("Congratulations Player " + player.id + "!! You've won the game!!")
    controller ! AbortGame
  }

}
