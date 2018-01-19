package model

import org.scalatest.Matchers._
import org.scalatest.WordSpec

class TestTileSet extends WordSpec {

  "A new series TileSet" should {
    val tile1 = new Tile("red", "", 1, false)
    val tile2 = new Tile("red", "", 2, false)
    val tile3 = new Tile("red", "", 3, false)
    var tiles = List[Tile](tile1,tile2,tile3)
    var tileSet = new TileSet(tiles, true)
    "contain 3 tiles" in {
      assert(tileSet.tiles.size == 3)
    }

    "be a Series" in {
      assert(tileSet.series == true)
    }
  }

  "A new not series TileSet " should {
    val tile1 = new Tile("yellow", "", 12, false)
    val tile2 = new Tile("red", "", 12, false)
    val tile3 = new Tile("black", "", 12, false)
    var tiles = List[Tile](tile1,tile2,tile3)
    var tileSet = new TileSet(tiles, false)

    "contain 3 tiles" in {
      assert(tileSet.tiles.size == 3)
    }

    "be a Series" in {
      assert(tileSet.series == false)
    }
  }

  "After appending a Tile the TileSet" should {
    val tile1 = new Tile("red", "", 1, false)
    val tile2 = new Tile("red", "", 2, false)
    val tile3 = new Tile("red", "", 3, false)
    var tiles = List[Tile](tile1,tile2,tile3)
    var tileSet = new TileSet(tiles, true)

    tileSet.append(new Tile("red", "", 4, false))
    " contain 1 Tail more" in {
      assert(tileSet.tiles.size == 4)
    }
  }

}

