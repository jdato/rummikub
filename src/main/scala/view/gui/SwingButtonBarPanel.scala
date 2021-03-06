package view.gui

import java.awt.{Color, Font}

import akka.actor.ActorSelection
import model.Messages._

import scala.swing.event.ButtonClicked
import scala.swing.{Button, GridPanel}

class SwingButtonBarPanel(controller: ActorSelection) extends GridPanel(1,4) {

  background = Color.WHITE

  val arialFont = new Font("Arial", Font.CENTER_BASELINE, 12)

  val startBtn = new Button("Start")
  val passBtn = new Button("Pass")
  val checkBtn = new Button("Check")
  val quitBtn = new Button("Quit")

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
    case _: ButtonClicked => controller ! Quit      //Simulation GameOver(new Player(new Rack(List()), 12))
  }

  contents += startBtn
  contents += passBtn
  contents += checkBtn
  contents += quitBtn
}