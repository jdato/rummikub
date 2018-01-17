package controller

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import model.Messages._
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import view.gui.SwingGuiViewActor
import view.tui.TuiView

class ControllerTest extends TestKit(ActorSystem("rummikupAS")) with ImplicitSender with FlatSpecLike with Matchers with BeforeAndAfterAll {

  private val actorSystemName = "rummikupAS"
  private val controllerActorName = "controller"

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A game" should "start" in {
    val actorSystem = ActorSystem.create(actorSystemName)
    val controller = actorSystem.actorOf(Props[Controller], controllerActorName)

    //var tui = actorSystem.actorOf(Props[TuiView], "tuiActor")
    //var gui = actorSystem.actorOf(Props[SwingGuiViewActor], "guiActor")

    controller ! RegisterObserver

    controller ! StartGame
    expectMsgAnyOf(PrintMessage("\nGame Started!\n"))
    expectMsgAnyOf(PrintMessage("Pool initialized!"))
    expectMsgAnyOf(PrintMessage("Players initialized!"))
    expectMsgAnyOf(PrintMessage("Gambling for the starting position."))


    /*
    controller ! Pass()
    expectMsgAnyOf(PrintMessage("passed, next Player."))
    */
  }

}