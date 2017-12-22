package controller
import model.{Player, Playingfield, Tile, TileSet}
import view.gui.ScalaFxGui

class GraphicalGame extends GameTrait {

  val utils: Utils = new Utils

  override def playMove(possibleMoves: List[TileSet], player: Player, plaingfield: Playingfield): Unit = ???


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
      //playMove(checkMoves(player), player, playingfield)
    }

    ScalaFxGui.passMovesButton.onAction = event => {
      player.pass = true
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
    //TODO remove this statement
    false
  }

  override def gambleForStartingPositon(players: Set[Player], pool: Set[Tile]): Player = {

    ScalaFxGui.printLine("Gambling for the starting position.")

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
        ScalaFxGui.printLine("Player " + p.id + " picked: ")
        ScalaFxGui.printTilesToFieldPane(List(tile))
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
      if (jokerPicked) ScalaFxGui.printLine("Joker picked. Repeating start.")
      if (starter.count(p => true) > 1) ScalaFxGui.printLine("More than one highest tile. Repeating start.")
    } while (starter.count(p => true) > 1 || jokerPicked)

    val startPlayer = starter.head
    ScalaFxGui.printLine("Player " + startPlayer.id + " starts.")
    startPlayer
  }
}
