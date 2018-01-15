package view.gui

import java.awt.{Color, ComponentOrientation, Font}

import akka.actor.ActorSelection
import model.Messages.SetTiles
import model.{Player, Playingfield, Tile, TileSet}

import scala.collection.mutable.ListBuffer
import scala.swing.event.ButtonClicked
import scala.swing.{Alignment, Button, Dimension, Font, GridPanel, Label, Swing}

class SwingGamePanel(controller: ActorSelection) extends GridPanel(4, 0) {

  val arialFont = new Font("Arial", Font.CENTER_BASELINE, 20)
  val darkGreyBackground = Color.LIGHT_GRAY
  val panelBorder = Swing.LineBorder(Color.BLACK)
  val emptyTileText = ""


  // Playingfield
  def printPlayingField(playingfield: Playingfield): Unit = {

    playingFieldPane.contents.clear()
    playingFieldPane.contents.clear()

    val tileSetRow = new GridPanel(20, 20) {
      background = darkGreyBackground
      componentOrientation = ComponentOrientation.LEFT_TO_RIGHT
    }

    var rows: ListBuffer[GridPanel] = ListBuffer()

    playingfield.playedTileSets.foreach(playedTileSet => {

      playedTileSet.tiles.foreach(tile => {

        val color = matchColor(tile.color)

        val realTile = new Label() {
          //icon = new ImageIcon("images/tile.png")
          text = tile.number.toString
          font = arialFont
          foreground = color
          verticalAlignment = Alignment.Center
        }

        playingFieldPane.contents += realTile
      })

      val numberOfColsToFill: Int = tileSetRow.columns - playedTileSet.tiles.size

      for (i <- 1 to numberOfColsToFill) {
        val fillTile = new Label() {
          //icon = new ImageIcon("images/tile.png")
          text = emptyTileText
          font = arialFont
          verticalAlignment = Alignment.Center
        }

        playingFieldPane.contents += fillTile
      }
    })
  }

  val playingFieldPane = new GridPanel(0, 20) {
    border = panelBorder
  }


  // Possible Sets
  def printPossibleSets(tileSets: List[TileSet]): Unit = {

    possibleSetsPane.contents.clear()

    val tileSetRow = new GridPanel(20, 20) {
      background = darkGreyBackground
      componentOrientation = ComponentOrientation.LEFT_TO_RIGHT
    }

    var number: Int = tileSets.size

    tileSets.foreach(possibleTileSet => {

      val num = number

      possibleSetsPane.contents += new Button() {
        minimumSize = new Dimension(15, 5)
        text = "s" + num
        reactions += {
          case _: ButtonClicked => {
            controller ! SetTiles("s" + num)
            possibleSetsPane.contents.clear()
            possibleSetsPane.visible_=(true)
          }
        }
      }

      number = number - 1

      possibleTileSet.tiles.foreach(tile => {

        val color = matchColor(tile.color)

        val realTile = new Label() {
          //icon = new ImageIcon("images/tile.png")
          text = tile.number.toString
          font = arialFont
          foreground = color
          verticalAlignment = Alignment.Center
        }

        possibleSetsPane.contents += realTile
      })

      val numberOfColsToFill: Int = tileSetRow.columns - possibleTileSet.tiles.size - 1

      for (i <- 1 to numberOfColsToFill) {
        val fillTile = new Label() {
          //icon = new ImageIcon("images/tile.png")
          text = emptyTileText
          font = arialFont
          verticalAlignment = Alignment.Center
        }

        possibleSetsPane.contents += fillTile
      }

    })
  }

  var possibleSetsPane = new GridPanel(0, 20) {
    border = panelBorder
  }


  // Possible Appends
  def printPossibleAppends(tileToSet: Map[Tile, TileSet]): Unit = {

    possibleAppendsPane.contents.clear()

    val tileSetRow = new GridPanel(20, 20) {
      background = darkGreyBackground
      componentOrientation = ComponentOrientation.LEFT_TO_RIGHT
    }

    // tileToSet.size and then count down
    var number: Int = 1

    tileToSet.foreach(possibleAppend => {

      val num = number

      possibleAppendsPane.contents += new Button() {
        minimumSize = new Dimension(15, 5)
        text = "a" + num
        reactions += {
          case _: ButtonClicked => {
            controller ! SetTiles("a" + num)
            possibleAppendsPane.contents.clear()
            possibleAppendsPane.visible_=(true)
          }
        }
      }

      // norm count down
      number = number + 1

      // Print the tile
      val color = matchColor(possibleAppend._1.color)

      val appendTile = new Label() {
        //icon = new ImageIcon("images/tile.png")
        text = possibleAppend._1.number.toString
        font = arialFont
        foreground = color
        verticalAlignment = Alignment.Center
      }

      possibleAppendsPane.contents += appendTile


      // Print the divider label
      val divider = new Label() {
        text = "to"
        font = arialFont
        foreground = Color.DARK_GRAY
        verticalAlignment = Alignment.Center
      }

      possibleAppendsPane.contents += divider


      // Print the Tileset
      possibleAppend._2.tiles.foreach(tile => {
        val color = matchColor(tile.color)

        val realTile = new Label() {
          //icon = new ImageIcon("images/tile.png")
          text = tile.number.toString
          font = arialFont
          foreground = color
          verticalAlignment = Alignment.Center
        }

        possibleAppendsPane.contents += realTile
      })

      val numberOfColsToFill: Int = tileSetRow.columns - possibleAppend._2.tiles.size - 3

      for (i <- 1 to numberOfColsToFill) {
        val fillTile = new Label() {
          //icon = new ImageIcon("images/tile.png")
          text = emptyTileText
          font = arialFont
          verticalAlignment = Alignment.Center
        }

        possibleAppendsPane.contents += fillTile
      }

    })
  }

  val possibleAppendsPane = new GridPanel(0, 20) {
    border = panelBorder
  }


  // Rack
  def printRack(player: Player): Unit = {

    rackPane.contents.clear()

    player.rack.tiles.foreach(tile => {

      val color = matchColor(tile.color)

      val label = new Label() {
        //icon = new ImageIcon("images/tile.png")
        text = tile.number.toString
        font = arialFont
        foreground = color
      }

      rackPane.contents += label
    })
  }

  def matchColor(color: String): Color = color match {
    case "red" => Color.RED
    case "blue" => Color.BLUE
    case "yellow" => Color.YELLOW
    case "black" => Color.BLACK
    case _ => Color.WHITE
  }

  val rackPane = new GridPanel(1, 40) {
    border = panelBorder
    preferredSize = new Dimension(60, 60)
    background = darkGreyBackground
  }

  contents += playingFieldPane
  contents += possibleSetsPane
  contents += possibleAppendsPane
  contents += rackPane
}
