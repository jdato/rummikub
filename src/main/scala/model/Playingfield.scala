package model

/**
  * Created by johannesdato on 10.11.17.
  */
class Playingfield {
  var playedTileSets : List[TileSet] = List()

  def playTileSet(tileSet: TileSet): Unit = {
    playedTileSets.::=(tileSet)
  }
}