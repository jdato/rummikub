package controller
import model.{Player, Playingfield, Tile, TileSet}
import view.gui.scalafx.ScalaFxGui

class GraphicalGame(_numberOfPlayers: Int, utils: Utils) {
  var playingfield: Playingfield = new Playingfield
  val numberOfPlayers: Int = _numberOfPlayers

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
    var starter = gambleForStartingPositon(players, utils.pool)
    val positionBeforeStarter = utils.selectStarterPosition(starter.id, numberOfPlayers)

  }

  def playMove(move: List[TileSet], player: Player, playingfield: Playingfield): Unit = {
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
  def printPlayingField(player: Player, playingfield: Playingfield): Unit = {
    ScalaFxGui.setPlayer(player)
    for (playedTileSet <- playingfield.playedTileSets) {
      ScalaFxGui.printTilesToFieldPane(playedTileSet.tiles)
    }
    ScalaFxGui.printTilesToRack(player.rack.tiles)
  }

  def play(player: Player, playingfield: Playingfield, checkMoves: Player => List[TileSet], players: Set[Player]): Boolean = {
    var possibleMoves = checkMoves(player)
    var tilesToAppand: List[Tile] = List[Tile]()
    player.rack.addTile(utils.pickRandomTile())
    printPlayingField(player, playingfield)

    ScalaFxGui.checkMovesButton.onAction = event => {
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
            play(player, playingfield, checkMoves, players)
          }
        })
      } else {
        ScalaFxGui.printLineToInstructionPane("No possible moves detected! Pass move.")
      }
    }

    ScalaFxGui.passMovesButton.onAction = event => {
      //switch player
      //print field
    }
    true
  }

  def gambleForStartingPositon(players: Set[Player], pool: Set[Tile]): Player = {

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
        val tile = p.pickInitTile(utils.pickRandomTile())
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
