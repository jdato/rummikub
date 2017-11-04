## Das Rummikub Spiel in Scala implementiert

Hier soll eine Iplementierung des Spiels Rummikub in Scala entstehen. Die Rahmenbedingungen dazu finden sich im Folgenden. 

#### Anforderungen

- Die Spielgrundlage sind 106 Spielsteine
- Es gibt vier Farben (blau, rot, gelb, schwarz)
- In jeder Farbe gibt es die Zahlen 1 - 13 in zweifacher Ausführung
- Es gibt zwei Joker (rot, schwarz)
- Jeder Spieler hat eine Ablage für seine Spielsteine (n - m Spieler)
- Es gibt einen Pool von Steinen aus dem zufällig Spielsteine entnommen und zurück gelegt werden können (nur Relevat für erste Runde) 

#### Spielaufbau

- Jeder spieler zieht einen Stein und der Spieler mit dem höchsten Wert beginnt
  - Bei gleichen Werten wird dieser Schritt wiederholt
- Anschließend werden im Uhrzeigersinn Steine aus dem Pool genommen bis jeder Spieler 14 auf der Ablage hat
- Die Steine sollten nach Farbe und Nummern Sortiert werden können

#### Spielregeln

- Die Spielsteine werden auf einem Spielfeld ausgespielt, dabei verschiebt der Spieler die Steine von seiner Ablage auf das Spielfeld
- Es müssen immer mindestens drei Steine zusammen ausgespielt werden
- Steine können in zwei verschiedenen Varianten auf dem Spielfeld ausgespielt werden
  1. Es wird eine Reihe von Zahlen der gleichen Farbe gespielt. Die Reihe kann auch über die Intervallgrenze (13] hinaus, wiederrum beginnend mit der 1, gespielt werden 
  2. Es wird eine Kombination von gleichen Zahlen in verschiedenen Farben gespielt. Dabei darf jede Farbe nur einmal in einer Kombination vorkommen
- Die beiden Joker können jeden beliebigen Spielstein ersetzen, solange die oben genannten Kriterien erfüllt bleiben
  
#### Spielablauf

- Damit ein Spieler seinen ersten Zug machen kann benötigt er Steine die gemeinsam mindestens einen Wert von 30 besitzen
- Der Gesamtwert ergibt sich aus der Summe der Werte auf den Spielsteinen
- Nachdem der erste Zug gemacht ist, kann ein Spieler seine Steine auf dem Feld ausspielen
  - Das eröffnen neuer Reihen muss mit den Kriterien in den Spielregeln konform sein
  - Es können auch einzelne Steine an Reihen angelegt werden oder Reihen gesplittet werden und die Steine eingefügt, es müssen aber wiederrum die Kriterien in den Spielregeln erhalten bleiben
- In einem Zug spielt ein Spieler entweder Steine oder zieht einen neuen Stein aus dem Pool
- Wenn ein Spieler keinen Zug machen kann, muss er einen Spielstein aus dem Pool nehmen und auf seine Ablage legen. Damit ist der Zug ebenfalls beenet

### Modell Klassen

package game
  - Game
    - Setup setup
    - boolean started
    - Player activePlayer
    - Player[] gameLoop
    - 
  - Setup
    - Set<Set<List>> PlayingField
    - List<Tiles> pool (Singleton hier werden alle Tiles initial erstellt)
    - List<Player> player
    - Game game


package model
  - PlayingField
    - Set<List> TilesByColor
  - TilesByColor
    - Enum color (blue, red, yellow, black, mixed(only for field))
    - List tiles
  - Player
    - Rack rack
    - int id
    - String name
    
package items
  - Tile
    - Enum color (blue, red, yellow, black)
    - int number (0 - 13)
    - boolean joker
  - TileSet
    - List tiles
    - SetType setType
        - boolean numericalSeries
        - int maxTiles (4, (26 !überlegen))  
    - (method) split
    - (method) append
    - (method) reduce
  - Rack
    - List tiles
    - (method) sortNumbers, sortColors
    - (method) removeTiles
    - (method) addTile
    