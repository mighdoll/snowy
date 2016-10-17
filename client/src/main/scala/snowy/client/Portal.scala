package snowy.client

import snowy.playfield._
import vector.Vec2d


class Portal3(rect: Rect, mySled: Vec2d) {
  var sleds =  Seq[Sled]()
  var trees = Seq[Sled]()
  var snowballs = Seq[Sled]()
  def apply(sleds: Seq[Sled], snowballs: Seq[Snowball], trees: Seq[Tree]): Portal3 = {
    new Portal3(rect, mySled)
  }
}


object Sample2 {
  val balls: Store[Snowball] = ???
  val sleds: Store[Sled] = ???
  val trees: Store[Tree] = ???
  val mySled: Sled = ???
//  val portal = new Portal3(mySled.pos, screenSize)(balls.items, sleds.items, trees.items)
//  portal.translateToScreen()
//  drawState(portal.balls, portal.sleds, portal.trees)
}


//object Drawer {
//  def drawBall(pos: Vec2d, ball: Snowball): Unit = ???
//}
//
//import Drawer._
//
//object Sample {
//  val balls: Store[Snowball] = ???
//  val mySled: Sled = ???
//  val portal = Portal(mySled.pos)
//
//
//  balls.items.foreach { ball =>
//    val pos = portal.translateToScreen(ball)
//    drawBall(pos, ball)
//  }
//
//}
//
//
//object Portal {
//  def apply(center: Vec2d, portalSize: Vec2d): Portal = ???
//}
//
///** Viewable area of the game playfield
//  *
//  * @param rect game playfield coordinates of the portal
//  */
//
//case class Portal(rect: Rect) {
//  def translateToScreen(item: PlayfieldObject): Vec2d = {
//    item.pos + Vec2d(10, 10)
//  }
//}
