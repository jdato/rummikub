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
    tiles = tiles.sortWith((x,y) => x.color > y.color)
  }
  def removeTiles(tile: Tile): Unit = {
    if(tiles.contains(tile)) tiles.drop(tiles.indexOf(tile))
  }
  def addTile(tile: Tile): Unit ={
    tiles.::(tile)
  }
  def print():Unit = {
    for (t <- tiles) t.printTile()
  }
}

object Rack {

}
