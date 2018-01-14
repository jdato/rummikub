package view.gui

import java.awt.{Color, ComponentOrientation, Font}

import akka.actor.ActorSelection
import model.{Player, Playingfield}

import scala.collection.mutable.ListBuffer
import scala.swing.{Alignment, Dimension, Font, GridPanel, Label, ScrollPane}

class SwingGamePanel(cotroller: ActorSelection) extends GridPanel(3, 0) {

  val arialFont = new Font("Arial", Font.CENTER_BASELINE, 20)

  val playingFieldPane = new GridPanel(0, 20) {

  }

  def printPlayingField(playingfield: Playingfield): Unit = {

    playingFieldPane.contents.clear()

    val tileSetRow = new GridPanel(20, 20) {
      background = Color.LIGHT_GRAY
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
          text = "(-)"
          font = arialFont
          verticalAlignment = Alignment.Center
        }

        playingFieldPane.contents += fillTile
      }
    })

    //playingFieldPane.contents += tileSetRow
  }


  val playedSetsPane = new ScrollPane() {
    maximumSize = new Dimension(5, 5)
    background = Color.GRAY
  }


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
    preferredSize = new Dimension(60, 60)
    background = Color.LIGHT_GRAY
  }

  contents += playingFieldPane
  contents += playedSetsPane
  contents += rackPane

}
