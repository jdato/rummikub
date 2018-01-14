package model


object Messages {

  case class Init()
  case class StartGame()
  case class AbortGame()

  case class Input(input: String)

  case class RegisterObserver()

  case class PrintMessage(s : String) // Observer prints the message
  case class PrintPlayingField(player: Player, playingfield: Playingfield)
  case class PrintTilesHorizontally(tiles : List[Tile])
  case class PrintTile(tile: Tile)

  case class PrintControllerStatusMessage(message: String)
}
