package view.gui

import java.awt.{Color, Dimension}

import akka.actor.ActorSelection

import scala.swing.{GridPanel, ScrollPane}

class SwingGamePanel(cotroller: ActorSelection) extends GridPanel(3, 0) {

  background = Color.LIGHT_GRAY

  for(value <- 0 until this.rows){
    contents += new ScrollPane() {
      preferredSize = new Dimension(60, 60)

    }
  }

}
