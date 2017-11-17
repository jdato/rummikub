package model

/**
  * Created by johannesdato on 10.11.17.
  */
class Player(theRack : Rack, theId : Int) {
  var rack: Rack = theRack
  var id: Int = theId
  //var name = theName
  def print():Unit = {
    println("Player" + id)
    rack.printRack()
  }
}


object Player {

}
