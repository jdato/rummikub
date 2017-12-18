package ui.gui

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.text.{Font, Text}
import javafx.scene.control.{ToggleButton => JfxToggleBtn}

import scalafx.event.{ActionEvent, EventHandler}

object ScalaFxGui extends JFXApp {

  lazy val splitPane = new SplitPane {
    orientation = new Orientation(Orientation.Vertical)
    id = "gamePane"
    items.addAll(playingfieldPane, possibleSetsPane, rackPane)
  }
  val startButton = new Button {
    minWidth = 120
    minHeight = 66
    id = "playButton"
    text = "Play!"
    font = Font.apply("Arial Black", 20)
    disable = true
  }
  val quitButton = new Button {
    minWidth = 120
    minHeight = 66
    id = "quitButton"
    text = "Quit Game"
    font = Font.apply("Arial Black", 20)
    disable = true
  }
  val playingfieldPane = new ScrollPane {
    vgrow = Priority.Always
    hgrow = Priority.Always
    fitToWidth = true
    minHeight = 200
    id = "playingfield-pane"

    content = new VBox {
      vgrow = Priority.Always
      hgrow = Priority.Always
      children = new Text("Playing Field Pane.")
    }
  }
  val possibleSetsPane = new ScrollPane {
    vgrow = Priority.Always
    hgrow = Priority.Always
    fitToWidth = true
    minHeight = 200
    id = "possiblesets-pane"

    content = new VBox {
      vgrow = Priority.Always
      hgrow = Priority.Always
      children = new Text("Possible Sets Pane.")
    }
  }
  val rackPane = new ScrollPane {
    vgrow = Priority.Always
    hgrow = Priority.Always
    fitToWidth = true
    minHeight = 200
    id = "rack-pane"

    content = new VBox {
      vgrow = Priority.Always
      hgrow = Priority.Always
      children = new Text("Rack Pane.")
    }
  }

  val tog = new ToggleGroup {
    selectedToggle.onChange(
      (_, oldValue, newValue) => {
        startButton.text = "Play! (" + newValue.asInstanceOf[JfxToggleBtn].getText + ")"
        startButton.disable = false
      }
    )
  }
  val toggleGroup = new HBox {
    margin = Insets.apply(0, 0, 0, 12)
    spacing = 2
    children = List(
      new ToggleButton {
        minWidth = 50
        text = "2"
        toggleGroup = tog
      },
      new ToggleButton {
        minWidth = 50
        text = "3"
        toggleGroup = tog
      },
      new ToggleButton {
        minWidth = 50
        text = "4"
        toggleGroup = tog
      })
  }
  val playerSelect = new VBox {
    margin = Insets.apply(10, 0, 0, 0)
    children = List(
    new Label {
      text = "Number of players:"
      minWidth = 180
      style = "-fx-font-size: 1.5em"
    },
    toggleGroup
  )}

  stage = new PrimaryStage {
    title = "ScalaFX Rummikub Game"
    scene = new Scene(1020, 700) {
      //fill = Color.rgb(38, 38, 38)

      root = new BorderPane {
        top = new VBox {
          vgrow = Priority.Always
          hgrow = Priority.Always

          // Toolbar oben mit new button
          children = new ToolBar {
            prefHeight = 76
            maxHeight = 76
            id = "mainToolBar"
            content = List(
              new ImageView {
                image = new Image(
                  this.getClass.getResourceAsStream("/images/logo.png"))
                margin = Insets(0, 0, 0, 10)
              },
              new Region {
                minWidth = 20
              },
              playerSelect
              ,
              startButton
              ,
              quitButton
            )
          }
        }

        center = new BorderPane {
          center = splitPane
        }

      } // End of root BorderPane
    }
  }

  val loadAction = EventHandler

  //startButton.onAction = loadAction
}