package model

/**
  * Created by johannesdato on 10.11.17.
  */
class Rack(theTiles: List[Tile]){
  var tiles: List[Tile] = theTiles
  def sortNumbers(): Unit = {
    println("Implement sort numbers method.")
  }
  def sortColors(): Unit = {
    println("Implement sort colors method.")
  }
  def removeTiles(tile: Tile): Unit = {
    println("Implement remove tile/s method.")
    tiles.drop(tiles.indexOf(tile))
  }
  def addTile(tile: Tile): Unit ={
    println("Implement add tile method.")
    tiles.::(tile)
  }
  def print():Unit = {
    for (t <- tiles) t.printTile()
  }
}

object Rack {

}
