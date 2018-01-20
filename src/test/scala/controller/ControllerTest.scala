package controller

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import model.Messages.{InvalidInput, Pass, PrintMessage, PrintPlayingField, Quit, RegisterObserver, StartGame, _}
import model.{Player, Playingfield, Rack, Tile}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

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

    /*
    test of the StartGame function
    after the first Text message all other Text messages should be ignored
     */
    controller ! StartGame

    expectMsg(PrintMessage("\nGame Started!\n"))

    expectMsg(PrintMessage("Pool initialized!"))

    expectMsg(PrintMessage("Players initialized!"))

    expectMsg(PrintMessage("Gambling for the starting position."))

    // ignore all PrintMessages and Print Tile Messages
    // messages are to random for testing correctly
    ignoreMsg {
      case _ : PrintMessage => true
      case _: PrintTile => true
    }

    val player = new Player(new Rack(List()), 1)
    expectMsgClass(PrintPlayingField(player, new Playingfield).getClass)
    ignoreNoMsg()

    controller ! Quit

    expectMsgAnyOf(
      PrintMessage("Game aborted, by player 1!"),
      PrintMessage("Game aborted, by player 2!"),
      PrintMessage("Game aborted, by player 3!"),
      PrintMessage("Game aborted, by player 4!")
    )

  }



}