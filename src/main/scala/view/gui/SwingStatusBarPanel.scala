package view.gui

import java.awt.{Color, Font}

import scala.swing.{GridPanel, Label}

class SwingStatusBarPanel extends GridPanel(3,1){

  background = Color.WHITE

  val arialFont = new Font("Arial", Font.CENTER_BASELINE, 14)

  val status: Label = new Label {
    font = arialFont
  }

  val alert: Label = new Label {
    foreground = Color.RED
    font = arialFont
  }

  contents += new Label("Status: ") {font = arialFont}
  contents += status
  contents += alert

  def setStatus(game: String): Unit = status.text = game

  def setAlert(alertMsg: String): Unit = alert.text = alertMsg

}
