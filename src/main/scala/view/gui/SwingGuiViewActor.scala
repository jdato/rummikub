package view.gui

import akka.actor.{Actor, ActorRef, ActorSelection}
import model.Messages.{Init, PrintMessage, RegisterObserver, StartGame}

class SwingGuiViewActor extends Actor {

  val controller: ActorSelection = context.system.actorSelection("user/controller")
  controller ! RegisterObserver

  val frame = new SwingGuiFrame(controller)

  override def receive: Receive = {
    case Init => init()
    case StartGame => println("start swing gui game")
    case PrintMessage(message: String) => frame.statusPanel.setStatus(message)
  }

  def init(): Unit = {
    frame.statusPanel.setStatus("Welcome to the Scala Rummicup Game! To start the game, press the 'start' button.")
    // Implement start button and controller ! StartGame
  }

}
