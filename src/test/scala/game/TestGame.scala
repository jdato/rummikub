package game

import model.{Player, Rack, Tile}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

class TestGame extends WordSpec {

  "checking for Series" should {
    var tiles : List[Tile]= List()
    var game : Game = new Game(2)
    val rack = new Rack(tiles)

    val tile1 = new Tile("red", 11, false)
    val tile2 = new Tile("red", 12, false)
    val tile3 = new Tile("red", 13, false)
    rack.addTile(tile1)
    rack.addTile(tile2)
    rack.addTile(tile3)
    var player: Player = new Player(rack, 1)

    "give back a TileSet" in {
      assert(game.checkSeries(player).nonEmpty)
    }
    "have the Flag \"isSeries\"" in {
      assert(game.checkSeries(player).head.series)
    }
  }
  "checking for Series" should {
    var tiles: List[Tile] = List()
    var game: Game = new Game(2)
    val rack = new Rack(tiles)

    val tile1 = new Tile("red", 1, false)
    val tile2 = new Tile("red", 2, false)
    val tile3 = new Tile("red", 3, false)
    rack.addTile(tile1)
    rack.addTile(tile2)
    rack.addTile(tile3)
    var player: Player = new Player(rack, 1)

    "give back no Tileset if the player hasn´t made the first move ande there ist no Tileset over 30 Points" in {
      assert(game.checkSeries(player).isEmpty)
    }
    "have still the Flag \"madeFirstMove\"" in {
      assert(!player.madeFirstMove)
    }
  }
  "checking for Set" should {
    var tiles : List[Tile]= List()
    var game : Game = new Game(2)

    val rack = new Rack(tiles)
    val tile1 = new Tile("red", 10, false)
    val tile2 = new Tile("blue", 10, false)
    val tile3 = new Tile("black", 10, false)
    rack.addTile(tile1)
    rack.addTile(tile2)
    rack.addTile(tile3)
    var player: Player = new Player(rack, 1)

    "give back a TileSet" in {
      assert(game.checkSet(player).nonEmpty)
    }
    "have the Flag \"isSeries\"" in {
      assert(!game.checkSet(player).head.series)
    }
  }
}

