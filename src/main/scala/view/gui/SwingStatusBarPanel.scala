package view.gui

import java.awt.{Color, Font}

import scala.swing.{GridPanel, Label}

class SwingStatusBarPanel extends GridPanel(2,1){

  background = Color.WHITE

  val arialFont = new Font("Arial", Font.CENTER_BASELINE, 12)

  val status: Label = new Label {
    font = arialFont
  }
  contents += status

  def setStatus(game: String): Unit = status.text = "Status: " + game

}
