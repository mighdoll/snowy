import ClientMain.Size
import GameClientProtocol.{PlayField, Position, State, Trees}
import org.scalajs.dom

object ClientDraw {

  class snowFlake(ctx: dom.CanvasRenderingContext2D, index: Double, size: Size) {
    var x = index * 10
    var y = -Math.random() * size.height
    val flakeSize = Math.random() * 5 + 5

    def move(): Unit = {
      y += 1
      y = y % size.height
    }

    def draw(): Unit = {
      ctx.fillStyle = "#b3e5fc"
      ctx.beginPath()
      ctx.arc(x, y, flakeSize, 0, 2 * Math.PI)
      ctx.fill()
    }
  }

  //Draw a sled at an x and y
  def drawSled(ctx: dom.CanvasRenderingContext2D, name: String, pos: GameClientProtocol.Position, cannonRotation: Double, rotation: Double): Unit = {
    val x = pos.x
    val y = pos.y
    val turretSize = 50.0

    //Global strokeStyle
    ctx.strokeStyle = "rgb(100, 100, 100)"

    //Draw two skis
    ctx.lineCap = "round"
    ctx.lineWidth = turretSize * 9 / 55
    ctx.translate(x, y)
    ctx.rotate(-rotation)
    ctx.beginPath()
    ctx.moveTo(turretSize * 5 / 11, -turretSize * 25 / 22)
    ctx.lineTo(turretSize * 5 / 11, turretSize * 25 / 22)
    ctx.stroke()
    ctx.beginPath()
    ctx.moveTo(-turretSize * 5 / 11, -turretSize * 25 / 22)
    ctx.lineTo(-turretSize * 5 / 11, turretSize * 25 / 22)
    ctx.stroke()
    ctx.setTransform(1, 0, 0, 1, 0, 0)

    //Draw the barrel for snowballs
    ctx.translate(x + turretSize / 55, y)
    ctx.rotate(cannonRotation)
    ctx.lineWidth = 2.0
    ctx.fillStyle = "rgb(153, 153, 153)"
    ctx.beginPath()
    ctx.fillRect(-turretSize * 3 / 22, 0, turretSize * 3 / 11, turretSize * 9 / 10)
    ctx.strokeRect(-turretSize * 3 / 22, 0, turretSize * 3 / 11, turretSize * 9 / 10)
    ctx.fill()
    ctx.stroke()
    ctx.setTransform(1, 0, 0, 1, 0, 0)

    //Draw the main body
    ctx.fillStyle = "rgb(120, 201, 44)"
    ctx.beginPath()
    ctx.arc(x, y, turretSize / 2, 0, 2 * Math.PI)
    ctx.fill()
    ctx.stroke()

    //Draw the name
    ctx.font = (turretSize * 3 / 11) + "px Arial";
    ctx.beginPath()
    ctx.fillText(name, x - (ctx.measureText(name).width / 2), y - turretSize * 27 / 22)
    ctx.fill()
  }

  //Draw a tree on the canvas
  def drawTree(ctx: dom.CanvasRenderingContext2D, pos: GameClientProtocol.Position): Unit = {
    val x = pos.x
    val y = pos.y
    val branchSize = 100

    ctx.fillStyle = "rgb(94, 153, 105)"
    ctx.beginPath()
    ctx.moveTo(x, y - branchSize * 2)
    ctx.lineTo(x - branchSize * 1 / 2, y - branchSize * 1 / 2)
    ctx.lineTo(x + branchSize * 1 / 2, y - branchSize * 1 / 2)
    ctx.closePath()
    ctx.fill()

    ctx.fillStyle = "rgb(56, 85, 58)"
    ctx.beginPath()
    ctx.fillRect(x - branchSize * 1 / 16, y - branchSize * 3 / 4, branchSize * 1 / 8, branchSize * 3 / 4)
    ctx.fill()

    ctx.translate(x, y - branchSize * 29 / 50)
    ctx.rotate(Math.PI * 13 / 18)

    ctx.beginPath()
    ctx.fillRect(0, 0, branchSize * 1 / 20, branchSize * 43 / 200)
    ctx.fill()

    ctx.rotate(Math.PI * 5 / 9)
    ctx.beginPath()
    ctx.fillRect(0, branchSize * 7 / 200, branchSize * 1 / 20, branchSize * 1 / 4)
    ctx.fillRect(branchSize * -1 / 10, branchSize * -13 / 200, branchSize * 1 / 20, branchSize * 1 / 4)
    ctx.fill()

    ctx.setTransform(1, 0, 0, 1, 0, 0)
  }

  def drawSnowball(ctx: dom.CanvasRenderingContext2D, pos: GameClientProtocol.Position, size: Double): Unit = {
    ctx.strokeStyle = "rgb(100, 100, 100)"
    ctx.fillStyle = "rgb(208, 242, 237)"
    ctx.beginPath()
    ctx.arc(pos.x, pos.y, size, 0, 2 * Math.PI)
    ctx.fill()
    ctx.stroke()
  }

  def drawBorder(ctx: dom.CanvasRenderingContext2D, top: GameClientProtocol.Position, bottom: GameClientProtocol.Position): Unit = {
    ctx.beginPath()
    ctx.moveTo(top.x, top.y)
    ctx.lineTo(top.x, bottom.y)
    ctx.lineTo(bottom.x, bottom.y)
    ctx.lineTo(bottom.x, top.y)
    ctx.closePath()
    ctx.stroke()
  }

  def centerObject(size: Size, pos: GameClientProtocol.Position, me: GameClientProtocol.Position): GameClientProtocol.Position = {
    Position(pos.x - me.x + size.width / 2, pos.y - me.y + size.height / 2)
  }

  def addOffset(pos: GameClientProtocol.Position, offset: PlayField): GameClientProtocol.Position = {
    Position(pos.x + offset.width, pos.y + offset.height)
  }

  def drawState(size: Size, ctx: dom.CanvasRenderingContext2D, state: State, offset: PlayField, trees: Trees, border: PlayField): Unit = {
    //Draw all snowballs
    state.snowballs.foreach { snowball =>
      drawSnowball(ctx, addOffset(centerObject(size, snowball.position, state.mySled.position), offset), 10.0)
    }
    //Draw all sleds
    drawSled(ctx, "me", addOffset(Position(size.width / 2, size.height / 2), offset), state.mySled.turretRotation, state.mySled.rotation)
    state.sleds.foreach { sled =>
      drawSled(ctx, sled.userName, addOffset(centerObject(size, sled.position, state.mySled.position), offset), sled.turretRotation, sled.rotation)
    }

    trees.trees.foreach { tree =>
      drawTree(ctx, addOffset(centerObject(size, tree.position, state.mySled.position), offset))
    }

    drawBorder(ctx, addOffset(centerObject(size, GameClientProtocol.Position(0, 0), state.mySled.position), offset), addOffset(centerObject(size, GameClientProtocol.Position(border.width, border.height), state.mySled.position), offset))
  }
}
