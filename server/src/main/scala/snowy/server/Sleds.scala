package snowy.server

import snowy.playfield.{Playfield, Sled}

class Sleds(override protected val playfield: Playfield) extends GridItems[Sled] {}
