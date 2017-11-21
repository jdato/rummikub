package model

/**
  * Created by johannesdato on 10.11.17.
  */
class Player(theRack : Rack, theId : Int) {
  var rack: Rack = theRack
  var id: Int = theId
  val madeFirstMove: Boolean = false
  var pass: Boolean = true
  var initTile: Tile = new Tile("none", 0, false)
  //var name = theName
  def printPlayer():Unit = {
    println("Player" + id)
    rack.printRack()
  }

  def pickInitTile(tile: Tile) : Tile = {
    initTile = tile
    initTile
  }
}


object Player {

}
