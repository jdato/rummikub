package controller

import model.{Player, Playingfield, Tile, TileSet}

/**
  * Base Trait for the Game Classes with Textual- and Graphical User Interfaces
  */
trait GameTrait {

  def play(player: Player, playingfield: Playingfield, checkMoves: Player => List[TileSet]): Boolean
  def playMove(possibleMoves: List[TileSet], player: Player, playingfield: Playingfield)
  def printPlayingField(player: Player, playingfield: Playingfield)
  def gambleForStartingPositon(players: Set[Player], pool: Set[Tile]): Player

}
