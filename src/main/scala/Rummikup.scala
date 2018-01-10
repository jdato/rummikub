import akka.actor.{ActorSystem, Props}
import controller.Controller
import model.Messages.{PrintMessage, StartGame}
import view.tui.TuiView

object Rummikup {

  // Main class of the game in which the actor system gets started

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem.create("rummikupAS")
    val controller = actorSystem.actorOf(Props[Controller], "controller")
    var tui = actorSystem.actorOf(Props(new TuiView(controller)), "tuiActor")

    //controller.tell(StartGame, null)

    tui ! PrintMessage("###############################\n#        SCALA RUMMIKUP       #\n###############################")
  }


}
