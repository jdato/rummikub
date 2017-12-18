package controller
import model.{Player, Playingfield, TileSet}

class GraphicalGame extends GameTrait {
  override def playMove(possibleMoves: List[TileSet], player: Player, plaingfield: Playingfield): Unit = ???

  override def printPlayingField(player: Player, playingfield: Playingfield): Unit = ???

  override def play(player: Player, playingfield: Playingfield, checkMoves: Player => List[TileSet]): Boolean = ???
}
