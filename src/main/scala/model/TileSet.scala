package model

/**
  * Created by johannesdato on 10.11.17.
  */
class TileSet(theTiles : List[Tile], isSeries : Boolean) {

  var tiles: List[Tile] = theTiles
  var series: Boolean = isSeries

  def split(): Unit = {
    println("Implement split method.")
  }
  def append(): Unit = {
    println("Implement append method.")
  }
  def reduce(): Unit ={
    println("Implement reduce method.")
  }
}

object TileSet {

}
