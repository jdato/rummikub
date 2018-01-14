package view.gui.scalafx

import akka.actor.{Actor, ActorRef}
import model.Messages.{PrintMessage, RegisterObserver, StartGame}

import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.{JFXApp, Platform}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane

object GuiApp extends JFXApp {

  class GuiActor(controller: ActorRef) extends Actor {

    controller ! RegisterObserver

    override def receive: Receive = {
      case StartGame => start()
      case PrintMessage(message: String) => println(message)
      case s:String => Platform.runLater{
        label.text = "Message: " + s
      }
    }
  }

  def start(): Unit = {
    val args = Array.empty[String]
    main(args)
  }

  val label = new Label("Loading ...")

  stage = new PrimaryStage {
    title = "ScalaFX Rummikub Game"
    //scene = view.scene
    scene = new Scene {
      root = new BorderPane {
        padding = Insets(25)
        center = label
      }
    }
  }



  /*
  val model = new GuiModel()
  val view = new GuiView(model)
  val controller = new GuiController(model, view)

  controller.start()

  override def stopApp(): Unit = controller.exit()
*/
}
