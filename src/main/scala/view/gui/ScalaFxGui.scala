package view.gui

import javafx.scene.control.{ToggleButton => JfxToggleBtn}

import controller.{Game, GraphicalGame}
import model.{Player, Tile}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.text.Font
import scalafx.scene.{Node, Scene}

object ScalaFxGui extends JFXApp {


  var numberOfPlayers = 0;

  var playingFieldList = List(
    new Label {}
  )
  var rackList = List(
    new Label {}
  )

  def printTilesToFieldPane(tiles: List[Tile]) = {
    playingFieldList = List(new Label {})
    var space = ""
    var color = "black"
    tiles.foreach(t => {
      //make all tiles same space
      if (t.number < 10) space = " "
      else space = ""

      //make font readable
      if (t.color == "black" || t.color == "blue") color = "white"
      else if (t.color == "yellow" || t.color == "red") color = "black"

      playingFieldList = playingFieldList.::(
        new Label {
          text = space + t.number
          padding = Insets(10)
          style =
            "-fx-border-color: black;" +
              "-fx-font-size: 1.2em;" +
              "-fx-font-weight: bold;" +
              "-fx-background-color: " + t.color + ";" +
              "-fx-text-fill: " + color + ";"
        }
      )
    })
    var playingfieldPaneContendTilePane = new TilePane {
      vgrow = Priority.Always
      hgrow = Priority.Always
      children = playingFieldList
    }
    playingfieldPane.playingfieldPaneContend.children.add(playingfieldPaneContendTilePane)
  }

  def printTilesToRack(tiles: List[Tile]): Unit = {
    rackList = List(new Label {})
    var space = ""
    var color = "black"
    tiles.foreach(t => {
      //make all tiles same space
      if (t.number < 10) space = " "
      else space = ""

      //make font readable
      if (t.color == "black" || t.color == "blue") color = "white"
      else if (t.color == "yellow" || t.color == "red") color = "black"

      rackList = rackList.::(
        new Label {
          text = space + t.number
          padding = Insets(10)
          style =
            "-fx-border-color: black;" +
              "-fx-font-size: 1.2em;" +
              "-fx-font-weight: bold;" +
              "-fx-background-color: " + t.color + ";" +
              "-fx-text-fill: " + color + ";"
        }
      )
    })
    var rackPaneContendTilePane = new TilePane {
      vgrow = Priority.Always
      hgrow = Priority.Always
      children = rackList
    }
    rackPane.rackPaneContend.children.add(rackPaneContendTilePane)
  }

  def printLine(string: String): Unit = {
    var label = new Label {
      text = string
    }
    playingfieldPane.playingfieldPaneContend.children.add(label)
  }

  def printTile(t: Tile): Unit = {
    //make font readable
    var color = ""
    if (t.color == "black" || t.color == "blue") color = "white"
    else if (t.color == "yellow" || t.color == "red") color = "black"
    var tile = new Label {
      text = "" + t.number
      padding = Insets(10)
      style =
        "-fx-border-color: black;" +
          "-fx-font-size: 1.2em;" +
          "-fx-font-weight: bold;" +
          "-fx-background-color: " + t.color + ";" +
          "-fx-text-fill: " + color + ";"
    }

    playingfieldPane.playingfieldPaneContend.children.add(tile)
  }

  def makeLabel_Big(s: String): Node = {
    var label = new Label {
      text = s
      style =
        "-fx-font-size: 1.2em;" +
          "-fx-font-weight: bold;"
    }
    label
  }

  def setPlayer(player: Player) = {
    rackLabel = makeLabel_Big("Your Rack Player " + player.id + ":")
    playingFieldList = List(new Label {})
    rackList = List(new Label {})
  }

  lazy val splitedPane = new SplitPane {
    orientation = new Orientation(Orientation.Vertical)
    id = "gamePane"
    items.addAll(
      makeLabel_Big("PlayingField:"), playingfieldPane,
      makeLabel_Big("PossibleMoves:"), possibleSetsPane,
      rackLabel, rackPane)
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

  val checkMovesButton = new Button {
    minWidth = 120
    minHeight = 66
    id = "checkMovesButton"
    text = "Check Moves"
    font = Font.apply("Arial Black", 20)
    disable = false
  }

  val passMovesButton = new Button {
    minWidth = 120
    minHeight = 66
    id = "passMovesButton"
    text = "Pass Moves"
    font = Font.apply("Arial Black", 20)
    disable = false
  }

  var rackLabel = makeLabel_Big("Rack:")

  var playingfieldPane = new ScrollPane {
    vgrow = Priority.Always
    hgrow = Priority.Always
    fitToWidth = true
    minHeight = 200
    id = "playingfield-pane"

    var playingfieldPaneContend = new VBox {
      vgrow = Priority.Always
      hgrow = Priority.Always
      var playingfieldPaneContendTilePane = new TilePane {
        vgrow = Priority.Always
        hgrow = Priority.Always
      }
      children = List(playingfieldPaneContendTilePane)
    }
    content = playingfieldPaneContend
  }

  val possibleSetsPane = new ScrollPane {
    vgrow = Priority.Always
    hgrow = Priority.Always
    fitToWidth = true
    minHeight = 200
    id = "possiblesets-pane"
    var possibleSetsPaneContend = new VBox {
      vgrow = Priority.Always
      hgrow = Priority.Always
      var possibleSetsPaneContendTilePane = new TilePane {
        vgrow = Priority.Always
        hgrow = Priority.Always
      }
      children = List(possibleSetsPaneContendTilePane)
    }
    content = possibleSetsPaneContend
  }

  val rackPane = new ScrollPane {
    vgrow = Priority.Always
    hgrow = Priority.Always
    fitToWidth = true
    minHeight = 70
    id = "rack-pane"
    var rackPaneContend = new VBox {
      vgrow = Priority.Always
      hgrow = Priority.Always

      var rackPaneContendTilePane = new TilePane {
        vgrow = Priority.Always
        hgrow = Priority.Always
      }
      children = List(rackPaneContendTilePane)
    }
    content = rackPaneContend
  }

  val tog = new ToggleGroup {
    selectedToggle.onChange(
      (_, oldValue, newValue) => {
        numberOfPlayers = newValue.asInstanceOf[JfxToggleBtn].getText.toInt
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
    )
  }

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
          center = splitedPane
        }

        bottom = new HBox {
          margin = Insets.apply(12)
          spacing = 2
          children = List(
            checkMovesButton,
            passMovesButton
          )
        }
      } // End of root BorderPane
    }
  }

  startButton.onAction = (event: ActionEvent) => {
    var game = new Game(numberOfPlayers, new GraphicalGame)
    game.startGame()
  }
}