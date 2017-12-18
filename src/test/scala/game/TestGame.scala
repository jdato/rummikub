package game

import controller.Game
import model.{Rack, Tile}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

class TestGame extends WordSpec {

  "checking for Series" should {
    var tiles : List[Tile]= List()
    var game : Game = new Game(2)

    val rack = new Rack(tiles)
    val tile1 = new Tile("red", 1, false)
    val tile2 = new Tile("red", 2, false)
    val tile3 = new Tile("red", 3, false)
    rack.addTile(tile1)
    rack.addTile(tile2)
    rack.addTile(tile3)

    "give back a TileSet" in {
      assert(game.checkSeries(rack).nonEmpty)
    }
    "have the Flag \"isSeries\"" in {
      assert(game.checkSeries(rack).head.series)
    }
  }
  "checking for Set" should {
    var tiles : List[Tile]= List()
    var game : Game = new Game(2)

    val rack = new Rack(tiles)
    val tile1 = new Tile("red", 1, false)
    val tile2 = new Tile("blue", 1, false)
    val tile3 = new Tile("black", 1, false)
    rack.addTile(tile1)
    rack.addTile(tile2)
    rack.addTile(tile3)

    "give back a TileSet" in {
      assert(game.checkSet(rack).nonEmpty)
    }
    "have the Flag \"isSeries\"" in {
      assert(!game.checkSet(rack).head.series)
    }
  }
}

