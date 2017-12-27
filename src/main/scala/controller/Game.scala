package controller

import java.util.Random

import model._

/**
  * Created by johannesdato on 10.11.17.
  */
class Game(_numberOfPlayers: Int, _gameType: GameTrait) {

  var playingfield: Playingfield = new Playingfield
  val numberOfPlayers: Int = _numberOfPlayers
  val gameType: GameTrait = _gameType
  var utils: Utils = new Utils

  var pool: Set[Tile] = Set[Tile]()
  var players: Set[Player] = Set()
  var started = false
  var activePlayerId = 1

  def startGame(): Unit = {
    // calls init method
    initializePool()
    initializePlayers()

    // Enters the game loop
    println("Game started.")
    started = true

    var firstRound = true
    var starter = gameType.gambleForStartingPositon(players, pool)
    val positionBeforeStarter = selectStarterPosition(starter.id)

    while (started) {
      for (player <- players) {
        if (!firstRound) {
          player.pass = false
          player.rack.addTile(utils.pickRandomTile(pool))
          gameType.printPlayingField(player, playingfield)
          started = gameType.play(player, playingfield, checkMoves)
          if (!started) {
            abortGame(player)
            return
          }
        }
        else {
          if (player.id == positionBeforeStarter) firstRound = false
        }
      }
    }
  }

  def selectStarterPosition(starter: Int): Int = starter match {
    case 1 => numberOfPlayers
    case 2 => 1
    case 3 => 2
    case 4 => 3
  }

  def abortGame(p: Player): Unit = {
    // Exits the game loop
    println("Game aborted, by player " + p.id + "!")
    started = false
  }

  // Initializes the pool and sets all the available stones in it
  def initializePool(): Unit = {
    var color = 0
    var twoDecks = 0
    var number = 0
    var numberCode = 0

    /*val red = "\u001B[31m"
    val blue = "\u001B[34m"
    val yellow = "\u001B[33m"
    val black = "\u001B[30m"*/

    for (color <- 1 to 4) {
      var currColor = matchColor(color)
      var currColorCode = matchColorCode(color)
      for (twoDecks <- 1 to 2) {
        for (number <- 1 to 13) {
          val tile = new Tile(currColorCode, currColor, number, false)
          pool.+=(tile)
        }
      }
    }

    def matchColor(x: Int): String = x match {
      case 1 => "red"
      case 2 => "blue"
      case 3 => "yellow"
      case 4 => "black"
      case _ => "no color"
    }

    def matchColorCode(x: Int): String = x match {
      case 1 => "\u001B[31m"
      case 2 => "\u001B[34m"
      case 3 => "\u001B[33m"
      case 4 => "\u001B[30m"
      case _ => "no color"
    }
    /*
        // Add the jokers
        val joker = new Tile(red, 0, true)
        pool.+=(joker)
        joker.color = black
        pool.+=(joker)
    */
    println("Pool initialized.")
  }

  //Initializes the players
  def initializePlayers(): Unit = {
    for (i <- 1 to numberOfPlayers) {
      var player = new Player(initializeRack(), i)
      //player.print()
      players.+=(player)
    }
  }

  //Initializes the rack and set random Tiles
  def initializeRack(): Rack = {
    var tiles: Set[Tile] = Set()
    for (i <- 1 to 14) {
      tiles.+=(pickRandomTileFromPool())
    }
    new Rack(tiles.toList) // Implicit return statement
  }

  //take a random Tile out of the pool
  def pickRandomTileFromPool(): Tile = {
    var poolArray = pool.toArray
    var tile = poolArray(new Random().nextInt(pool.size))
    pool.-=(tile)
    return tile
  }

  //checking for same colored series in the rack
  def checkSeries(player: Player): List[TileSet] = {
    //TODO method ignores jockers
    var rack = player.rack
    var tileSets: List[TileSet] = List[TileSet]()
    var tiles: List[Tile] = List[Tile]()

    var number: Int = 0
    var count: Int = 0
    var color: String = ""

    rack.sortNumbers()
    rack.sortColors()
    //set first color
    color = rack.tiles.head.colorCode
    for (tile <- rack.tiles) {
      if (color == tile.colorCode) {
        if (tile.number == number + 1) {
          count.+=(1)
          number.+=(1)
          tiles = tiles.::(tile)
        }
        else if (tile.number == number) {
          //nothing to do
        }
        else {
          count = 1
          number = tile.number
          tiles = List[Tile]().::(tile)
        }
      } else {
        count = 1
        color = tile.colorCode
        tiles = List[Tile]().::(tile)
        number = tile.number
      }
      if (count >= 3) {
        if (player.madeFirstMove || checkSum(tiles)) {
          tileSets = tileSets.::(new TileSet(tiles, true))
        }
      }
    }
    return tileSets
  }

  // checking for any Sets in the rack
  def checkSet(player: Player): List[TileSet] = {
    //TODO method ignores jockers and same colored Tiles
    var rack = player.rack
    var tileSets: List[TileSet] = List[TileSet]()
    var tiles: List[Tile] = List[Tile]()
    var number: Int = 0
    var count: Int = 0
    var tilesToSet = List[Tile]()

    rack.sortNumbers()
    for (tile <- rack.tiles) {
      if (number == tile.number) {
        count.+=(1)
        tiles = tiles.::(tile)
      } else {
        number = tile.number
        count = 1
        tiles = List[Tile]().::(tile)
      }
      if (count >= 3) {
        if (player.madeFirstMove || checkSum(tiles)) {
          tileSets = tileSets.::(new TileSet(tiles, false))
        }
      }
    }
    return tileSets
  }

  // check if player has 30 Points
  def checkSum(tiles: List[Tile]): Boolean = {
    return tiles.foldLeft(0) { (z, i) => z + i.number } >= 30
  }

  //check if Rack contains a street or a Set
  def checkMoves(player: Player): List[TileSet] = {
    var tileSets: List[TileSet] = List[TileSet]()
    tileSets = tileSets.:::(checkSeries(player))
    tileSets = tileSets.:::(checkSet(player))
    tileSets
  }
}