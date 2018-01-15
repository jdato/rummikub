package model

/**
  * Created by johannesdato on 10.11.17.
  */
class Tile(theColorCode: String, theColor: String, theNumber: Int = 0, isJoker: Boolean) {
  var colorCode: String = theColorCode
  var color: String = theColor
  var number: Int = theNumber
  var joker: Boolean = isJoker
  val reset = "\u001B[0m"

  def printTile(): Unit = {
    print(colorCode + " --- \n")
    if(number > 0) {
      if(number < 10) print("| " + number +" |\n")
      else print("| " + number +"|\n")
    }
    if(number == 0) print("| ☺ |\n")
    //if(number == 0) print("|•‿•|\n")
    print("| ® |\n --- \n" + reset)
  }
}