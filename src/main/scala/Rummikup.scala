import akka.actor.{ActorSystem, Props}
import controller.Controller
import model.Messages.{Init, Input, PrintMessage}
import view.gui.SwingGuiViewActor
import view.tui.TuiView

object Rummikup {

  // Main class of the game in which the actor system gets started

  def main(args: Array[String]): Unit = {

    val actorSystem = ActorSystem.create("rummikupAS")

    val controller = actorSystem.actorOf(Props[Controller], "controller")
    var tui = actorSystem.actorOf(Props[TuiView], "tuiActor")
    var gui = actorSystem.actorOf(Props[SwingGuiViewActor], "guiActor")

    tui ! Init
    gui ! Init

    while (true) {
      val input = scala.io.StdIn.readLine()
      tui ! Input(input)
    }
  }

}
