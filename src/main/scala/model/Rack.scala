package model

/**
  * Created by johannesdato on 10.11.17.
  */
class Rack(theTiles: List[Tile]){
  var tiles: List[Tile] = theTiles

  def sortNumbers(): Unit = {
    tiles = tiles  sortBy (_.number)
  }
  def sortColors(): Unit = {
    tiles = tiles.sortWith((x, y) => x.colorCode < y.colorCode)
  }
  def removeTile(tile: Tile): Unit = {
    if(tiles.contains(tile)) {
      tiles = tiles.filter(_ != tile)
    }
  }
  def addTile(tile: Tile): Unit ={
    tiles.::=(tile)
    sortNumbers()
    sortColors()
  }
  def printRack():Unit = {
    for (t <- tiles) t.printTile()
  }
}