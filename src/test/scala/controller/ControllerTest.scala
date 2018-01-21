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
      case _ : PrintTile => true
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


  "A game" should "run with no errors" in {
    val actorSystem = ActorSystem.create(actorSystemName)
    val controller = actorSystem.actorOf(Props[Controller], controllerActorName)

    ignoreMsg {
      case _ : PrintMessage => true
      case _ : PrintTile => true
      case _ : PrintPlayingField => true
      case _ : PrintControllerStatusMessage => true
      case _ : PrintPossibleAppendsToTileSets => true
      case _ : PrintPossibleTileSets => true
      case _ : PrintTilesHorizontally => true
    }

    controller ! RegisterObserver

    controller ! StartGame

    controller ! Check
/*
    controller ! Pass

    controller ! Check
*/
    for (i <- 1 to 40) {
      controller ! Pass
    }

    controller ! Check

    controller ! SetTiles("s1")

    controller ! Pass

    controller ! Check

    controller ! SetTiles("s1")

    controller ! Pass

    controller ! Check

    controller ! SetTiles("s1")

    controller ! Pass

    controller ! Check

    controller ! SetTiles("s1")

    // Made 1st move

    controller ! Check

    controller ! SetTiles("a1")

    controller ! Check

    controller ! SetTiles("s1")

    controller ! Check

    controller ! SetTiles("a1")

    // Done

    controller ! Pass

    controller ! Check

    controller ! SetTiles("a1")

    controller ! Pass

    controller ! Check

    controller ! SetTiles("a1")

    controller ! Pass

    controller ! Check

    controller ! SetTiles("a1")

    controller ! Pass

    controller ! Check

    controller ! SetTiles("a1")

    controller ! Pass

    controller ! Check

    controller ! SetTiles("a1")

    controller ! Pass

    controller ! Check

    controller ! SetTiles("a1")

    controller ! Pass

    controller ! Check

    controller ! SetTiles("a1")

    controller ! Pass

    controller ! Check

    controller ! SetTiles("a1")

    controller ! InvalidInput

    controller ! GameOver(new Player(new Rack(List()), 1))

    controller ! Quit

  }
}