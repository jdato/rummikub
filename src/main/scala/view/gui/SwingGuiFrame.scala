package view.gui

import akka.actor.ActorSelection
import model.Messages.StartGame

import scala.swing.{Action, BorderPanel, Dimension, Frame, Menu, MenuBar, MenuItem}

class SwingGuiFrame(controller: ActorSelection) extends Frame{

  val statusPanel = new SwingStatusBarPanel
  val gamePanel = new SwingGamePanel(controller)
  val buttonPanel = new SwingButtonBarPanel(controller)

  contents = new BorderPanel {

    layout(gamePanel) = BorderPanel.Position.Center
    layout(statusPanel) = BorderPanel.Position.North
    layout(buttonPanel) = BorderPanel.Position.South

  }

  title = "The Scala Rummikub Game"
  peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
  size = new Dimension(800, 1200)
  visible = true
}
