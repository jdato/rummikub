package controller

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import model.Messages.{InvalidInput, Pass, Quit, RegisterObserver, StartGame, _}
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
    var player = new Player(new Rack(List()), 1)
    expectMsg(PrintMessage("\nGame Started!\n"))
    // ignore all PrintMessages and Print Tile Messages
    // messages are to random for testing correctly
    ignoreMsg ({
      case msg: PrintMessage => true
      case msg: PrintTile => true
    })
    expectMsgClass(PrintPlayingField(player, new Playingfield).getClass)
    //stop ignoring all PrintMessages
    ignoreNoMsg()

    /*
    test of the pass function
    after the first Text message all other Text messages should be ignored
     */
    controller ! Pass
    expectMsg(PrintMessage("passed, next Player."))

    expectMsgClass(PrintMessage("").getClass)
    expectMsgClass(PrintPlayingField(player, new Playingfield).getClass)


    /*
    test of the printControllerStatusMessage function
    commented cause only send simple System.out.println()
     */
    //controller ! PrintControllerStatusMessage("message")
    //TODO ? runs simple System.out.println() methode

    /*/*
    test of the pass function
     */
    controller ! Check
    //TODO
    ignoreMsg ({
      case msg: PrintPossibleAppendsToTileSets ⇒ true
      case msg: PrintPossibleTileSets ⇒ true
      case msg: PrintMessage ⇒ true
    })
    expectMsgClass(PrintPossibleAppendsToTileSets(Map()).getClass)
    expectMsgClass(PrintPossibleTileSets(List()).getClass)

    ignoreNoMsg()
    */


    /*
    test of the invalidImput function
     */
    controller ! InvalidInput
    expectMsg(PrintMessage("invalid Input"))

    /*
    test of the SetTiles function
    hard to test cause not possible to detect if and which tileset is possible to set
     */
    //controller ! SetTiles("s1")
    //expectMsg(PrintMessage("passed, next Player."))


    /*
    test of the gameover function
     */
    var tiles : List[Tile]= List()
    val rack = new Rack(tiles)
    controller ! GameOver(new Player(rack,1))
    expectMsgClass(GameOver(new Player(rack,1)).getClass)


    /*
    test of the quit function
     */
    controller ! Quit
    expectMsgClass(PrintMessage("").getClass)
    expectMsgClass(AbortGame.getClass)
  }

}