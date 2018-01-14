package view.gui

import akka.actor.ActorSelection
import model.Messages.{PrintControllerStatusMessage, PrintMessage, StartGame}

import scala.swing.event.Key
import scala.swing.{Action, BorderPanel, Dimension, Frame, Menu, MenuBar, MenuItem}

class SwingGuiFrame(controller: ActorSelection) extends Frame{

  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.M
      contents += new MenuItem(Action("New") {
        controller ! PrintControllerStatusMessage("New Button Clicked")
      })
      contents += new MenuItem(Action("Quit") {
        controller ! PrintControllerStatusMessage("Quit Button Clicked")
      })
      contents += new MenuItem(Action("Start") {
        controller ! StartGame
      })
    }
  }

  val statusPanel = new SwingStatusBarPanel
  val gamePanel = new SwingGamePanel(controller)

  contents = new BorderPanel {

    //layout(letterTopBarPanel) = BorderPanel.Position.North
    //layout(numberSideBarPanel) = BorderPanel.Position.West
    //layout(gamePanel) = BorderPanel.Position.Center
    layout(statusPanel) = BorderPanel.Position.North

  }

  title = "The Scala Rummikup Game"
  peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
  size = new Dimension(800, 800)
  visible = true
}
