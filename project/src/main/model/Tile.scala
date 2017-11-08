package model

object Colours extends Enumeration {
  val Red, Green, Blue, Yellow = Value
}
case class Tile(color:Colours, number:Int, joker:Boolean)

case class TileSet(tiles:List[Tile])

case class Rack(tiles:List[Tile]){
  def sortNumbers(): List[Tile] =
    if (tiles!=null )tiles.sortBy(tile.number)
    else false

  def sortColors(): List[Tile] =
    if (tiles!=null )tiles.sortBy(tile.color)
    else null

  def removeTile(tile:Tile): List[Tile] =
    if (tiles.containsSlice(tile))tiles.drop(indexOf(tile))
    else false

  def addTile(tile:Tile: List[Tile] =
    if (tiles.containsSlice(tile))tiles
    else new List[Tile](tiles,tile)
}