package model

/**
  * Created by johannesdato on 10.11.17.
  */
class Player(theRack : Rack, theId : Int) {
  var rack: Rack = theRack
  var id: Int = theId
  var madeFirstMove: Boolean = false
  var pass: Boolean = false
  var initTile: Tile = new Tile("none", "none", 0, false)

  def pickInitTile(tile: Tile) : Tile = {
    initTile = tile
    initTile
  }
}