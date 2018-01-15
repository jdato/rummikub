package model
import org.scalatest.Matchers._
import org.scalatest.WordSpec

class TestRack extends WordSpec {


  "A new Rack " should {
    val rack = new Rack(Nil)
    "be empty" in {
      assert(rack.tiles.isEmpty)
    }
  }

  "After adding a Tile, the rack  " should {
    var tiles : List[Tile]= List()
    val rack = new Rack(tiles)
    rack.addTile(new Tile("red", "", 3, false))
    "not be empty" in {
      assert(rack.tiles.nonEmpty)
    }
    "have one Tile" in {
      assert(rack.tiles.size == 1)
    }
  }
  "After removing a Tile, the rack  " should {
    var tiles : List[Tile]= List()
    val rack = new Rack(tiles)
    val tile = new Tile("red", "", 3, false)
    rack.addTile(tile)
    rack.removeTile(tile)
    "be empty" in {
      assert(rack.tiles.isEmpty)
    }
  }

  "After removing a spezific Tile, the rack  " should {
    var tiles : List[Tile]= List()
    val rack = new Rack(tiles)
    val tile1 = new Tile("red", "", 3, false)
    val tile2 = new Tile("red", "", 2, false)
    val tile3 = new Tile("red", "", 1, false)
    rack.addTile(tile1)
    rack.addTile(tile2)
    rack.addTile(tile3)
    rack.removeTile(tile2)
    "not contain this tile" in {
      assert(!rack.tiles.contains(tile2))
    }
    "but contain the other tiles" in {
      assert(rack.tiles.contains(tile1))
      assert(rack.tiles.contains(tile3))
    }
  }

  "After sorting by number  " should {
    var tiles : List[Tile]= List()
    val rack = new Rack(tiles)
    val tile1 = new Tile("red", "", 1, false)
    val tile2 = new Tile("blue", "", 2, false)
    val tile3 = new Tile("red", "", 3, false)
    val tile4 = new Tile("green", "", 0, true)
    rack.addTile(tile1)
    rack.addTile(tile2)
    rack.addTile(tile3)
    rack.addTile(tile4)
    rack.sortNumbers()
    "be sorted by number" in {
      var number = 0
      for(tile <- rack.tiles){
        assert(tile.number>=number)
        number = tile.number
      }
    }
  }
  "After sorting by color  " should {
    var tiles : List[Tile]= List()
    val rack = new Rack(tiles)
    val tile1 = new Tile("red", "", 1, false)
    val tile2 = new Tile("blue", "", 2, false)
    val tile3 = new Tile("red", "", 3, false)
    val tile4 = new Tile("green", "", 0, true)
    rack.addTile(tile1)
    rack.addTile(tile2)
    rack.addTile(tile3)
    rack.addTile(tile4)
    rack.sortNumbers()
    rack.sortColors()
    "be sorted by color" in {
      rack.printRack()
      assert(rack.tiles.indexOf(tile3) > rack.tiles.indexOf(tile1))
      assert(rack.tiles.indexOf(tile1) > rack.tiles.indexOf(tile4))
      assert(rack.tiles.indexOf(tile4) > rack.tiles.indexOf(tile2))
    }
  }
}

