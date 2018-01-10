package controller
import model.{Player, Playingfield, Tile, TileSet}
import view.gui.ScalaFxGui

class GraphicalGame extends GameTrait {

  val utils: Utils = new Utils

  override def playMove(move: List[TileSet], player: Player, playingfield: Playingfield): Unit = {
    var tileSet = move.head

    playingfield.playTileSet(tileSet)
    ScalaFxGui.printTilesToFieldPane(tileSet.tiles)
    for (tile <- tileSet.tiles) {
      player.rack.removeTile(tile)
    }
    player.madeFirstMove = true
  }

  /**
    * Print the playingfield and the players rack on the ScalaFX UI
    *
    * @param player
    * @param playingfield
    */
  override def printPlayingField(player: Player, playingfield: Playingfield): Unit = {
    ScalaFxGui.setPlayer(player)
    for (playedTileSet <- playingfield.playedTileSets) {
      ScalaFxGui.printTilesToFieldPane(playedTileSet.tiles)
    }
    ScalaFxGui.printTilesToRack(player.rack.tiles)
  }

  override def play(player: Player, playingfield: Playingfield, checkMoves: Player => List[TileSet]): Boolean = {
    var abort = false
    player.pass = false

    ScalaFxGui.checkMovesButton.onAction = event => {

    }

    ScalaFxGui.passMovesButton.onAction = event => {
      player.pass = true
    }

    var possibleMoves = checkMoves(player)
    var tilesToAppand: List[Tile] = List[Tile]()

    if (player.madeFirstMove) {

      player.rack.tiles.foreach(tile => {
        val tileSet = utils.checkAppend(tile, playingfield)
        if (tileSet != null) {
          tilesToAppand.::=(tile)
          ScalaFxGui.printLineToInstructionPane("Append Tile:")
          ScalaFxGui.printTileToInstructionPane(tile)
          ScalaFxGui.printLineToInstructionPane("to the TileSet:")
          ScalaFxGui.printTilesToInstructionPane(tileSet.tiles)
        }
      })
    }
    if (possibleMoves.nonEmpty || tilesToAppand.nonEmpty) {
      ScalaFxGui.printLineToInstructionPane("Play TileSet:")
      possibleMoves.foreach(possibleMove => {
        var a = ScalaFxGui.printTilesToInstructionPane(possibleMove.tiles).onMouseClicked = event => {
          playMove(List({
            possibleMove
          }), player, playingfield)
          play(player, playingfield, checkMoves)
        }
      })
    } else {
      ScalaFxGui.printLineToInstructionPane("No possible moves detected! Pass move.")
    }


    //TODO hier hängt der Controller in Endlosschleife
    // while not passing its your turn
    /*while (!player.pass && !abort) {
      // check victory
      if (player.rack.tiles.size == 0) {
        abort = true
        ScalaFxGui.printLine("Congratulations Player" + player.id + ", you have won!")
        return false
      }
      wait(100)
    }*/
    if (abort) false
    else true
    false
  }

  override def gambleForStartingPositon(players: Set[Player], pool: Set[Tile]): Player = {

    ScalaFxGui.printLineToInstructionPane("Gambling for the starting position.")

    var starter: List[Player] = List()
    var jokerPicked = false

    // Repeat if two player pick the same number or a joker has been picked
    do {
      starter = List()
      jokerPicked = false
      var initTiles: List[Tile] = List()
      // Initial picking of numbers
      players.foreach(p => {
        val tile = p.pickInitTile(utils.pickRandomTile(pool))
        initTiles.::=(tile)
        ScalaFxGui.printLineToInstructionPane("Player " + p.id + " picked: ")
        ScalaFxGui.printTileToInstructionPane(tile)
      })
      val maxByVal = initTiles.maxBy(tile => tile.number)
      players.foreach(player => {
        if (player.initTile.number == maxByVal.number) {
          starter.::=(player)
        }
        if (player.initTile.number == 0) {
          jokerPicked = true
        }
      })
      if (jokerPicked) ScalaFxGui.printLineToInstructionPane("Joker picked. Repeating start.")
      if (starter.count(p => true) > 1) ScalaFxGui.printLineToInstructionPane("More than one highest tile. Repeating start.")
    } while (starter.count(p => true) > 1 || jokerPicked)

    val startPlayer = starter.head
    ScalaFxGui.printLineToInstructionPane("Player " + startPlayer.id + " starts.")
    startPlayer
  }
}
