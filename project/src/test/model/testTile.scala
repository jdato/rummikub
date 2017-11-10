package model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class testTile extends WordSpec with Matchers {
  "A Tile" when {
    "new" should {
      val tile = Tile(Red, 3, false)
      "have a color" in {
        tile.color should be(Red)
      }
      "have a number" in {
        tile.number should be(3)
      }
      "is a Jocker or not" in {
        tile.joker should be(false)
      }
    }
  }
}
