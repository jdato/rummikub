package controller
import model.{Player, Playingfield, Tile, TileSet}

class GraphicalGame extends GameTrait {
  override def playMove(possibleMoves: List[TileSet], player: Player, plaingfield: Playingfield): Unit = ???


  /**
    * Print the playingfield and the players rack on the ScalaFX UI
    *
    * @param player
    * @param playingfield
    */
  override def printPlayingField(player: Player, playingfield: Playingfield): Unit = ???

  override def play(player: Player, playingfield: Playingfield, checkMoves: Player => List[TileSet]): Boolean = ???

  override def gambleForStartingPositon(players: Set[Player], pool: Set[Tile]): Player = ???
}
