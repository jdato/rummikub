package view.gui

import java.awt.Color

import akka.actor.ActorSelection

import scala.swing.{Button, Dimension, GridPanel, Rectangle, ScrollPane}

class SwingGamePanel(cotroller: ActorSelection) extends GridPanel(3, 0) {

  val playingFieldPane = new ScrollPane() {
    preferredSize = new Dimension(60, 60)
    background = Color.DARK_GRAY
  }

  val playedSetsPane = new ScrollPane() {
    preferredSize = new Dimension(60, 60)
    background = Color.GRAY
  }

  val rackPane = new ScrollPane(){
    preferredSize = new Dimension(60, 60)
    background = Color.LIGHT_GRAY
  }

  val rect = new Rectangle(4, 10)
  var btn = new Button("Tile")

  contents += playingFieldPane
  contents += playedSetsPane
  contents += rackPane

}
