package view.gui

import java.awt.{Color, Font}

import akka.actor.ActorSelection
import model.Messages._

import scala.swing.event.ButtonClicked
import scala.swing.{Button, GridPanel}

class SwingButtonBarPanel(controller: ActorSelection) extends GridPanel(1,5) {

  background = Color.WHITE

  val arialFont = new Font("Arial", Font.CENTER_BASELINE, 12)

  val startBtn = new Button("Start")
  val passBtn = new Button("Pass")
  val checkBtn = new Button("Check")
  val quitBtn = new Button("Quit")
  val testBtn = new Button("Test")

  startBtn.reactions += {
    case _: ButtonClicked => controller ! StartGame
  }
  passBtn.reactions += {
    case _: ButtonClicked => controller ! Pass
  }
  checkBtn.reactions += {
    case _: ButtonClicked => controller ! Check
  }
  quitBtn.reactions += {
    case _: ButtonClicked => controller ! Quit
  }
  testBtn.reactions += {
    case _: ButtonClicked =>
  }

  contents += startBtn
  contents += passBtn
  contents += checkBtn
  contents += quitBtn
  contents += testBtn // Panel anpassen ^^

}