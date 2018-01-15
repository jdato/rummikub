package model


object Messages {

  case class Init()
  case class StartGame()
  case class AbortGame()

  case class Input(input: String)
  case class Pass()
  case class Check()
  case class Quit()
  case class SetTiles(input: String)
  case class InvalidInput()

  case class RegisterObserver()

  case class PrintMessage(s : String)
  case class PrintPlayingField(player: Player, playingfield: Playingfield)
  case class PrintTilesHorizontally(tiles : List[Tile])
  case class PrintPossibleTileSets(tileSets : List[TileSet])
  case class PrintPossibleAppendsToTileSets(tilesToAppendToTileSet : Map[Tile, TileSet])
  case class PrintTile(tile: Tile)

  case class PrintControllerStatusMessage(message: String)
}
