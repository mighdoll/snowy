import GameClientProtocol.Position

/** convert 2d double vectors to integer client Positions */
object Vec2dClientPosition {

  implicit class ConvertVecToPosition(vec: Vec2d) {
    def toPosition: Position = Position(vec.x.toInt, vec.y.toInt)
  }

}