package model

import org.scalatest.Matchers._
import org.scalatest.WordSpec

class TestTile extends WordSpec {

  "A new Tile set to red 2 " should {
    val tile = new Tile("red", "", 2, false)

    "have color red" in {
      assert(tile.colorCode == "red")
    }

    "be not a joker" in {
      assert(!tile.joker)
    }

    "have the numer 2" in {
      assert(tile.number==2)
    }
  }
  "A new Tile set to black Joker " should {
    val tile = new Tile("black", "", 0, true)

    "have color black" in {
      assert(tile.colorCode == "black")
    }

    "be  a joker" in {
      assert(tile.joker)
    }

    "have the numer 0" in {
      assert(tile.number==0)
    }
  }
}

