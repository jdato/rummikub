package controller

import java.util.Random

import akka.actor.{Actor, ActorRef, Props}
import model.Messages._
import model._


class Controller extends Actor {

  def props = Props(new Controller())

  private val observers = scala.collection.mutable.SortedSet.empty[ActorRef]

  override def receive: Receive = {
    case PrintControllerStatusMessage(message: String) => printControllerStatusMessage(message)
    case StartGame => startGame()
    case RegisterObserver => observers += sender(); printControllerStatusMessage("Subscription from: [" + sender().toString() + "]")
    case Pass => pass()
    case Check => check()
    case Quit => quit()
    case GameOver(player: Player) => observers.foreach(_ ! GameOver(actualPlayer))
    case SetTiles(input: String) => setTiles(input)
    case InvalidInput => invalidInput()
  }

  val playingfield: Playingfield = new Playingfield

  // ##############################
  //    Set the number of players
  // ##############################
  val numberOfPlayers: Int = 4

  var players: Set[Player] = Set()
  var pool: Set[Tile] = Set[Tile]()
  var actualPlayer: Player = _
  var nextPlayer: Player = _
  var possibleMoves: List[TileSet] = List()
  var tilesToAppand: List[Tile] = List[Tile]()
  var tilesToAppendToTileSet: Map[Tile, TileSet] = Map()

  def printControllerStatusMessage(message: String): Unit = {
    println("\u001B[34m" + "CTRL: " + message + "\u001B[0m")
  }

  def pass(): Unit = {
    observers.foreach(_ ! PrintMessage("passed, next Player."))
    val next = getNextPlayer(actualPlayer)
    makeMove(next, actualPlayer, true)
  }

  def check(): Unit = {
    tilesToAppand = List()
    tilesToAppendToTileSet = Map()
    possibleMoves = checkMoves(actualPlayer)
    playMove(possibleMoves, actualPlayer, nextPlayer)
  }

  def quit(): Unit = {
    abortGame(actualPlayer)
  }

  def invalidInput(): Unit = {
    observers.foreach(_ ! PrintMessage("invalid Input"))
  }

  def setTiles(input: String): Unit = {

    observers.foreach(_ ! SetTiles(input))

      input.toList match {
        case 's' :: tileNumber :: Nil =>
          var number: Int = Integer.valueOf(tileNumber.toString)
          var i = 1: Int
          for (tileSet <- possibleMoves) {
            if (i == number) {
              playingfield.playTileSet(tileSet)
              for (tile <- tileSet.tiles) {
                actualPlayer.rack.removeTile(tile)
              }
              actualPlayer.madeFirstMove = true
            }
            i = i + 1
          }
          observers.foreach(_ ! PrintPlayingField(actualPlayer, playingfield))
        case 'a' :: tileNumber :: Nil =>
          var number: Int = Integer.valueOf(tileNumber.toString)
          var i = 1: Int
          tilesToAppand = tilesToAppand.sortWith((x, y) => x.colorCode < y.colorCode)
          tilesToAppand = tilesToAppand.sortWith((x, y) => x.number < y.number)
          for (tile <- tilesToAppand) {
            if (i == number) {
              var tileSet = checkAppend(tile, playingfield)
              tileSet.append(tile)
              actualPlayer.rack.removeTile(tile)
            }
            i = i + 1
          }
          observers.foreach(_ ! PrintPlayingField(actualPlayer, playingfield))
        case _ => observers.foreach(_ ! PrintMessage("False Input!!!"))
      }

    if(actualPlayer.rack.tiles.isEmpty) {
      observers.foreach(_ ! GameOver(actualPlayer))
    }
  }


  def startGame(): Unit = {
    observers.foreach(_ ! PrintMessage("\nGame Started!\n"))

    initializePool()
    initializePlayers(numberOfPlayers)

    gambleForStartingPositon(players, pool)
  }


  def makeMove(player: Player, next: Player, firstMove: Boolean): Unit = {
    observers.foreach(_ ! PrintMessage("Player " + player.id + " is playing."))

    if(firstMove && pool.nonEmpty) player.rack.addTile(pickRandomTile())

    observers.foreach(_ ! PrintPlayingField(player, playingfield))

    // Check if empty
    if (player.rack.tiles.isEmpty) {
      observers.foreach(_ ! PrintMessage("Congratulations Player" + player.id + ", you have won!"))
      abortGame(player)
    }
    actualPlayer = player
    nextPlayer = next
  }

  // Init Methods
  def initializePlayers(numberOfPlayers: Int): Unit = {
    for (i <- 1 to numberOfPlayers) {
      var player = new Player(initializeRack(), i)
      //player.print()
      players.+=(player)
    }
    observers.foreach(_ ! PrintMessage("Players initialized!"))
  }

  //Initializes the rack and set random Tiles
  def initializeRack(): Rack = {
    var tiles: Set[Tile] = Set()
    for (i <- 1 to 14) {
      tiles.+=(pickRandomTileFromPool())
    }
    new Rack(tiles.toList) // Implicit return statement
  }

  def initializePool(): Unit = {
    var color = 0
    var twoDecks = 0
    var number = 0
    var numberCode = 0

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
    observers.foreach(_ ! PrintMessage("Pool initialized!"))
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



  def abortGame(p: Player): Unit = {
    // Exits the game loop
    observers.foreach(_ ! PrintMessage("Game aborted, by player " + p.id + "!"))
    observers.foreach(_ ! AbortGame)
    context.system.terminate()
  }

  def pickRandomTile(): Tile = {
    val num = new Random().nextInt(pool.size)
    var i = 0: Int
    for (t <- pool) {
      if (i == num) {
        pool = pool - t
        return t
      }
      i = i + 1
    }
    null
  }

  def checkAppend(tile: Tile, playingfield: Playingfield): TileSet = {
    for (tileSet <- playingfield.playedTileSets) {
      if (tileSet.series) {
        //check if tile can be added at the top or bottom
        tileSet.tiles = tileSet.tiles sortBy (_.number)
        if (tileSet.tiles.head.colorCode == tile.colorCode) {
          if ((tileSet.tiles.head.number- 1) == tile.number || (tileSet.tiles.last.number + 1) == tile.number) {
            return tileSet
          }
        }
      } else {
        if (tileSet.tiles.head.number == tile.number) {
          return tileSet
        }
      }
    }
    null
  }

  //take a random Tile out of the pool
  def pickRandomTileFromPool(): Tile = {
    var poolArray = pool.toArray
    var tile = poolArray(new Random().nextInt(pool.size))
    pool = pool - tile
    tile
  }

  //checking for same colored series in the rack
  def checkSeries(player: Player): List[TileSet] = {
    // method ignores jockers
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
    rack.tiles.foreach(tile => {
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
    })
    tileSets
  }

  // checking for any Sets in the rack
  def checkSet(player: Player): List[TileSet] = {
    // method ignores jockers and same colored Tiles
    var rack = player.rack
    var tileSets: List[TileSet] = List[TileSet]()
    var tiles: List[Tile] = List[Tile]()
    var number: Int = 0
    var count: Int = 0
    var tilesToSet = List[Tile]()

    rack.sortNumbers()
    rack.tiles.foreach(tile => {
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
    })
    tileSets
  }

  // check if player has 30 Points
  def checkSum(tiles: List[Tile]): Boolean = {
    tiles.foldLeft(0) { (z, i) => z + i.number } >= 30
  }

  //check if Rack contains a street or a Set
  def checkMoves(player: Player): List[TileSet] = {
    var tileSets: List[TileSet] = List[TileSet]()
    tileSets = tileSets.:::(checkSeries(player))
    tileSets = tileSets.:::(checkSet(player))
    tileSets
  }




  def gambleForStartingPositon(players: Set[Player], pool: Set[Tile]): Unit = {

    observers.foreach(_ ! PrintMessage("Gambling for the starting position."))

    var starter: List[Player] = List()
    var jokerPicked = false

    // Repeat if two player pick the same number or a joker has been picked
    do {
      starter = List()
      jokerPicked = false
      var initTiles: List[Tile] = List()
      // Initial picking of numbers
      players.foreach(p => {
        val tile = p.pickInitTile(pickRandomTile())
        initTiles.::=(tile)
        observers.foreach(_ ! PrintMessage("Player " + p.id + " picked: "))
        observers.foreach(_ ! PrintTile(tile))
      })
      val maxByVal = initTiles.maxBy(tile => tile.number)
      players.foreach(p => {
        if (p.initTile.number == maxByVal.number) {
          starter.::=(p)
        }
        if (p.initTile.number == 0) {
          jokerPicked = true
        }
      })
      if (jokerPicked) observers.foreach(_ ! PrintMessage("Joker picked. Repeating start."))
      if (starter.count(p => true) > 1) observers.foreach(_ ! PrintMessage("More than one highest tile. Repeating start."))
    } while (starter.count(p => true) > 1 || jokerPicked)

    actualPlayer = starter.head
    nextPlayer = getNextPlayer(actualPlayer)
    observers.foreach(_ ! PrintMessage("Player " + actualPlayer.id + " starts."))

    makeMove(actualPlayer, nextPlayer, true)
  }

  def getNextPlayer(player: Player): Player = {
    var nextPlayersId = player.id + 1
    if(nextPlayersId > numberOfPlayers) nextPlayersId = 1
    players.filter(player => player.id == nextPlayersId).head
  }

  def playMove(possibleMoves: List[TileSet], player: Player, next: Player): Unit = {

    var i = 1: Int
    if (player.madeFirstMove) {
      player.rack.tiles.foreach(tile => {
        val tileSet = checkAppend(tile, playingfield)
        if (tileSet != null) {
          tilesToAppand.::=(tile)
          observers.foreach(_ ! PrintMessage("press \"a" + i + "\" to append Tile:"))
          observers.foreach(_ ! PrintTile(tile))
          observers.foreach(_ ! PrintMessage("to the TileSet:"))
          observers.foreach(_ ! PrintTilesHorizontally(tileSet.tiles))
          tilesToAppendToTileSet += ((tile, tileSet))
          i = i + 1
        }
      })
    }
    i = 1
    var tileSets: List[TileSet] = List()
    if (possibleMoves.nonEmpty || tilesToAppand.nonEmpty) {
      possibleMoves.foreach(tileSet => {
        observers.foreach(_ ! PrintMessage("press \"s" + i + "\" to play Tileset:"))
        observers.foreach(_ ! PrintTilesHorizontally(tileSet.tiles))
        tileSets.::=(tileSet)
        i = i + 1
      })
      // 1. Appends
      observers.foreach(_ ! PrintPossibleAppendsToTileSets(tilesToAppendToTileSet))
      // 2. Sets
      observers.foreach(_ ! PrintPossibleTileSets(tileSets))
      observers.foreach(_ ! PrintMessage("##########################################################################################"))
      observers.foreach(_ ! PrintMessage("p: Pass move, s#: Play TileSet number #, a#: to append Tile # to a TileSet"))
    } else {
      observers.foreach(_ ! PrintMessage("No possible moves detected!\np: Pass move, c: Check moves, q:Quit Game"))
      // 1. Appends
      observers.foreach(_ ! PrintPossibleAppendsToTileSets(tilesToAppendToTileSet))
      // 2. Sets
      observers.foreach(_ ! PrintPossibleTileSets(tileSets))

    }

    actualPlayer = player
    nextPlayer = next
  }

}
