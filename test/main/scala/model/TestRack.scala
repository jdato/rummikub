import model.Rack

class TestRack {
  @Test
  def testIsEmpty ={
    val rack = new Rack()
    assertTrue("Rack is empty", rack.tiles.isEmpty)
  }

  @Test
  def testAdd ={
    val rack = new Rack()
    rack.addTile(new Tile(red, 1, false))
    assertFalse("Rack is not empty", rack.tiles.isEmpty)
  }
}