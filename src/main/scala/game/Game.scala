package game

import java.util.Random

import model._

/**
  * Created by johannesdato on 10.11.17.
  */
class Game(_numberOfPlayers: Int) {

  var playingfield : Playingfield = new Playingfield
  var numberOfPlayers: Int = _numberOfPlayers
  var utils: Utils = new Utils

  var pool : Set[Tile] = Set[Tile]()
  var players : Set[Player] = Set()
  var started = false
  var activePlayerId = 1

  def startGame(): Unit = {
    // calls init method
    initializePool()
    initializePlayers()

    // Enters the game loop
    println("Game started.")
    started =true


    while(started){
      for(player<-players){
        player.pass = false
        player.rack.addTile(getRandomTile())
        printPlayingField(player)

        //while not passing its your turn
        while(!player.pass){
          var input:String = readLine()

          input match {
            //case player doesn´t want to set any Tile
            case "p" =>
              println("passed, next Player:")
              player.pass = true

            //case player want to set a Tile
            case "s" =>
              //TODO implement Method for setting Tiles

              println("set all possible Tiles")
              for(tileSet<-checkMoves(player.rack)){
                playingfield.playTileSet(tileSet)
                for(tile<-tileSet.tiles) player.rack.removeTile(tile)
              }
              printPlayingField(player)

            //case player wants to exit game
            case "q" =>
              println("Game aborted!")
              started = false
              abortGame()

            // invalid imput
            case _ => println("invalid Input")
          }
        }
      }
    }
  }

  def abortGame(): Unit = {
    // Exits the game loop
    println("Implement abort game method.")
  }

  // Initializes the pool and sets all the available stones in it
  def initializePool(): Unit = {
    var color = 0
    var twoDecks = 0
    var number = 0

    val red = "\u001B[31m"
    val blue = "\u001B[34m"
    val yellow = "\u001B[33m"
    val black = "\u001B[30m"

    for (color <- 1 to 4) {
      var currColor = matchColor(color)
      for (twoDecks <- 1 to 2) {
        for (number <- 1 to 13) {
          val tile = new Tile(currColor, number, false)
          pool.+=(tile)
        }
      }
    }

    def matchColor(x: Int) : String = x match {
      case 1 => red
      case 2 => blue
      case 3 => yellow
      case 4 => black
      case _ => "no color"
    }

    // Add the jokers
    val joker = new Tile(red, 0, true)
    pool.+=(joker)
    joker.color = black
    pool.+=(joker)

    println("Pool initialized.")
  }

  //Initializes the players
  def initializePlayers(): Unit = {
    for(i <- 1 to numberOfPlayers){
      var player = new Player(initializeRack(), i)
      //player.print()
      players.+=(player)
    }
  }

  //Initializes the rack and set random Tiles
  def initializeRack() : Rack = {
    var tiles : Set[Tile] = Set()
    for(i <- 1 to 14){
      tiles.+=(getRandomTile())
    }
    new Rack(tiles.toList) // Implicit return statement
  }

  //take a random Tile out of the pool
  def getRandomTile(): Tile = {
    //TODO get Random Tile from pool
    val num = new Random().nextInt(pool.size)
    var i = 0: Int
    for (t <- pool){
      if(i==num){
        pool.-=(t)
        return t
      }
      i=i+1
      null
    }
    //if no Tiles in pool
    //TODO handle no Tiles in pool
    null
  }

  // prints the whole pool
  def printPool(): Unit = {
    for (t <- pool) t.printTile()
    //pool.foreach((t: Tile) => t.printTile())
  }

  //prints the Playingfield for the specific Player
  def printPlayingField(player: Player): Unit ={
    println("\n\n\n\n\n##########################################################################################")
    println("Played Tile Sets:")
    for(playedTileSet <- playingfield.playedTileSets) utils.printTilesHorizontally(playedTileSet.tiles)
    println("##########################################################################################")
    println("Your Rack, Player " + player.id)
    player.rack.sortNumbers()
    player.rack.sortColors()
    utils.printTilesHorizontally(player.rack.tiles)
    println("##########################################################################################")
    println("p: Pass, s: Set Tile(s), q:Quit Game")
  }

  //checking for same colored series in the rack
  def checkSeries(rack: Rack): List[TileSet] = {
    //TODO method ignores jockers
    var tileSets: List[TileSet] = List[TileSet]()
    var tiles: List[Tile] = List[Tile]()

    var number: Int = 0
    var count: Int = 1
    var color: String = ""

    rack.sortNumbers()
    rack.sortColors()
    //set first color
    color=rack.tiles.head.color
    for (tile <- rack.tiles) {
      if (color == tile.color) {
        if (tile.number == number+1){
          count.+=(1)
          number.+=(1)
          tiles = tiles.::(tile)
        }
        else if (tile.number == number){
          //nothing to do
        }
        else {
          count = 1
          number = tile.number
          tiles = List[Tile]().::(tile)
        }
      } else {
        count = 1
        color = tile.color
        tiles = List[Tile]().::(tile)
        number = tile.number
      }
      if(count >= 3){
        tileSets = tileSets.::(new TileSet(tiles,true))
      }
    }
    return tileSets
  }

  // checking for any Sets in the rack
  def checkSet(rack: Rack): List[TileSet] = {
    //TODO method ignores jockers and same colored Tiles
    var tileSets: List[TileSet] = List[TileSet]()
    var number: Int = 0
    var count: Int = 1
    var tilesToSet =List[Tile]()

    rack.sortNumbers()
    for (tile <- rack.tiles) {
      if (number == tile.number) {
        count.+=(1)
      } else {
        number = tile.number
        count = 1
      }
      if(count >= 3){
        tilesToSet = rack.tiles.filter(_.number == number)
        tileSets = tileSets.::(new TileSet(tilesToSet,false))
      }
    }
    return tileSets
  }

  //check if Rack contains a street or a Set
  def checkMoves(rack: Rack): List[TileSet] = {
    var tileSets: List[TileSet] = List[TileSet]()
    tileSets = tileSets.:::(checkSeries(rack))
    tileSets = tileSets.:::(checkSet(rack))
    return tileSets
  }
}

object Game {

}

