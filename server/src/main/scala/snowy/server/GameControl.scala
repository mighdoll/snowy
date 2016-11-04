package snowy.server

import scala.collection.mutable
import scala.concurrent.duration._
import scala.math.min
import akka.util.ByteString
import snowy.Awards._
import snowy.GameClientProtocol._
import snowy.GameConstants.Friction.slowButtonFriction
import snowy.GameConstants.{Bullet, _}
import snowy.GameServerProtocol._
import snowy.collision.{SledSnowball, SledTree}
import snowy.playfield.GameMotion.{moveSleds, moveSnowballs, wrapInPlayfield}
import snowy.playfield.PlayId.SledId
import snowy.playfield._
import snowy.server.GameSeeding.randomSpot
import snowy.util.Perf
import snowy.util.Perf.time
import socketserve.{AppController, AppHostApi, ConnectionId}
import upickle.default._
import boopickle.Default._
import com.typesafe.scalalogging.{LazyLogging, Logger, StrictLogging}
import snowy.playfield.Picklers._
import vector.Vec2d
import snowy.{BasicSled, SledKind, StationaryTestSled}

class GameControl(api: AppHostApi) extends AppController with GameState with StrictLogging {
  val tickDelta = 20 milliseconds
  val turnDelta = (math.Pi / turnTime) * (tickDelta.toMillis / 1000.0)
  val messageIO = new MessageIO(api)
  val connections = mutable.Map[ConnectionId, ClientConnection]()
  import messageIO.sendMessage

  api.tick(tickDelta) {
    connections.values.foreach(_.refreshTiming())
    gameTurn()
  }

  (1 to 20).foreach { i =>
    userJoin(new ConnectionId, s"StationarySled:$i", StationaryTestSled, robot = true)
  }

  /** Called to update game state on a regular timer */
  private def gameTurn(): Unit = time("gameTurn") {
    val deltaSeconds = nextTimeSlice()
    recordTurnJitter(deltaSeconds)
    recoverHealth(deltaSeconds)
    recoverPushEnergy(deltaSeconds)
    applyCommands(deltaSeconds)
    expireSnowballs()
    snowballs = moveSnowballs(snowballs, deltaSeconds)

    val (newSleds, moveAwards) = moveSleds(sleds, deltaSeconds)
    sleds = newSleds
    val collisionAwards = checkCollisions()
    val died = collectDead()
    updateScore(moveAwards ++ collisionAwards ++ died)
    reapDead(died)
    currentState().collect {
      case (id, state) if state.mySled.id.user.exists(!_.robot) =>
        sendMessage(state, id)
    }
    sendScores()
  }

  private def recordTurnJitter(deltaSeconds:Double):Unit = {
    val secondsToMicros = 1000000
    val offset = 10 * secondsToMicros // library can't handle negative
    Perf.record("turnJitter",
      offset + (deltaSeconds * secondsToMicros).toLong - tickDelta.toMicros
    )
  }

  /** a new player has connected */
  override def open(id: ConnectionId): Unit = {
    connections(id) = new ClientConnection(id, messageIO)
    val clientPlayfield = Playfield(playfield.x.toInt, playfield.y.toInt)
    sendMessage(clientPlayfield, id)
    sendMessage(Trees(trees.toSeq), id)
  }

  /** Called when a connection is dropped */
  override def gone(connectionId: ConnectionId): Unit = {
    for {
      sledId <- sledMap.get(connectionId)
      sled <- sledId.sled
    } {
      sled.remove()
    }
    users.remove(connectionId)
    commands.commands.remove(connectionId)
    connections.remove(connectionId)
  }

  /** Process a [[GameServerMessage]] from the client */
  def handleMessage(id: ConnectionId, msg: GameServerMessage): Unit = {
    logger.trace(s"handleMessage: $msg received from client $id")
    msg match {
      case Join(name, sledKind) => userJoin(id, name, sledKind)
      case TurretAngle(angle)   => rotateTurret(id, angle)
      case Shoot                => shootSnowball(id)
      case Start(cmd)           => commands.startCommand(id, cmd)
      case Stop(cmd)            => commands.stopCommand(id, cmd)
      case Pong                 => connections(id).pongReceived()
      case ReJoin(sledKind)     => rejoin(id, sledKind)
      case TestDie              => reapSled(sledMap(id))
    }
  }

  /** decode received binary message then pass on to handler */
  override def message(id: ConnectionId, msg: String): Unit = {
    handleMessage(id, read[GameServerMessage](msg))
  }

  /** decode received binary message then pass on to handler */
  override def binaryMessage(id: ConnectionId, msg: ByteString): Unit = {
    handleMessage(id, Unpickle[GameServerMessage].fromBytes(msg.asByteBuffer))
  }

  private def newRandomSled(userName: String, sledKind: SledKind): Sled = {
    // TODO what if sled is initialized atop a tree?
    Sled(userName = userName, pos = randomSpot(), size = 35, speed = Vec2d(0, 0),
      rotation = downhillRotation, turretRotation = downhillRotation,
      kind = sledKind)
  }

  /** Called when a user sends her name and starts in the game */
  private def userJoin(id: ConnectionId, userName: String, sledKind: SledKind,
                       robot: Boolean = false)
  : Unit = {
    println(s"user joined: $userName  userCount:${users.size}")
    val user = new User(userName, createTime = gameTime, robot = robot)
    users(id) = user
    createSled(id, user, sledKind)
  }

  private def rejoin(id: ConnectionId, sledKind: SledKind): Unit = {
    users.get(id) match {
      case Some(user) =>
        println(s"user rejoined: ${user.name}")
        createSled(id, user, sledKind)
      case None       =>
        println(s"user not found to rejoin: $id")
    }
  }

  private def createSled(connctionId: ConnectionId, user: User,
                         sledKind: SledKind): Unit = {
    val sled = newRandomSled(user.name, sledKind)
    sleds = sleds.add(sled)
    sledMap(connctionId) = sled.id
  }


  /** Rotate the turret on a sled */
  private def rotateTurret(id: ConnectionId, angle: Double): Unit = {
    modifySled(id) { sled =>
      sled.copy(turretRotation = angle)
    }
  }

  private def modifySled(id: ConnectionId)(fn: Sled => Sled): Unit = {
    sledMap.get(id).foreach { sledId =>
      sleds = sleds.replaceById(sledId) { sled =>
        fn(sled)
      }
    }
  }

  private def shootSnowball(id: ConnectionId): Unit = {
    modifySled(id) { sled =>
      if (sled.lastShotTime + sled.minRechargeTime > gameTime) {
        sled
      } else {
        val launchAngle = sled.turretRotation + sled.bulletLaunchAngle
        val launchPos = sled.bulletLaunchPosition.rotate(sled.turretRotation)
        val direction = Vec2d.fromRotation(-launchAngle)
        val ball = Snowball(
          ownerId = sled.id,
          pos = wrapInPlayfield(sled.pos + launchPos),
          size = sled.bulletSize,
          speed = sled.speed + (direction * sled.bulletSpeed),
          spawned = gameTime,
          power = sled.bulletPower
        )
        snowballs = snowballs.add(ball)

        val recoilForce = direction * -sled.bulletRecoil
        val speed = sled.speed + recoilForce
        sled.copy(speed = speed, lastShotTime = gameTime)
      }
    }
  }

  /** Rotate a sled.
    *
    * @param rotate rotation in radians from current position. */
  private def turnSled(sled: Sled, rotate: Double): Sled = {
    // TODO limit turn rate to e.g. 1 turn / 50msec to prevent cheating by custom clients?
    val max = math.Pi * 2
    val min = -math.Pi * 2
    val rotation = sled.rotation + rotate
    val wrappedRotation =
      if (rotation > max) rotation - max
      else if (rotation < min) rotation - min
      else rotation
    sled.copy(rotation = wrappedRotation)
  }

  /** apply any pending but not yet cancelled commands from user actions,
    * e.g. turning or slowing */
  private def applyCommands(deltaSeconds: Double): Unit = {

    commands.foreachCommand { (id, command) =>
      modifySled(id) { sled =>
        command match {
          case Left  => turnSled(sled, turnDelta)
          case Right => turnSled(sled, -turnDelta)
          case Slow  =>
            val slow = new InlineForce(-slowButtonFriction * deltaSeconds, sled.maxSpeed)
            sled.copy(speed = slow(sled.speed))
          case Push  =>
            val pushForceNow = PushEnergy.force * deltaSeconds
            val pushEffort = deltaSeconds / PushEnergy.maxTime
            val push = new InlineForce(pushForceNow, sled.maxSpeed)
            pushSled(sled, pushForceNow, push, pushEffort)
        }
      }
    }
  }

  /** apply a push to a sled */
  private def pushSled(sled: Sled, pushForceNow: Double, push: InlineForce, effort: Double)
  : Sled = {
    if (effort < sled.pushEnergy) {
      val speed =
        if (sled.speed.zero) Vec2d.fromRotation(sled.rotation) * pushForceNow
        else push(sled.speed)
      val energy = sled.pushEnergy - effort
      sled.copy(speed = speed, pushEnergy = energy)
    } else {
      sled
    }
  }

  /** slowly recover some health points */
  private def recoverHealth(deltaSeconds: Double): Unit = {
    sleds = sleds.replaceItems { sled =>
      val deltaHealth = deltaSeconds / sled.healthRecoveryTime
      val newHealth = min(sled.maxHealth, sled.health + deltaHealth)
      sled.copy(health = newHealth)
    }
  }

  /** slowly recover some push energy */
  private def recoverPushEnergy(deltaSeconds: Double): Unit = {
    val deltaEnergy = deltaSeconds / PushEnergy.recoveryTime
    sleds = sleds.replaceItems { sled =>
      val energy = min(1.0, sled.pushEnergy + deltaEnergy)
      sled.copy(pushEnergy = energy)
    }
  }


  private def expireSnowballs(): Unit = {
    val now = System.currentTimeMillis()
    snowballs = snowballs.removeMatchingItems { snowball =>
      now > snowball.spawned + Bullet.lifetime
    }
  }

  /** @return the sleds with no health left */
  private def collectDead(): Traversable[SledDied] = {
    sleds.items.find(_.health <= 0).map { sled =>
      SledDied(sled.id)
    }
  }

  /** Notify clients whose sleds have been killed, remove sleds from the game */
  private def reapDead(dead: Traversable[SledDied]): Unit = {
    dead.foreach { case SledDied(sledId) =>
      reapSled(sledId)
    }
  }

  private def reapSled(sledId: SledId): Unit = {
    if (sledId.user.exists(!_.robot)) {
      val connectionId = sledId.connectionId
      connectionId.foreach(sendMessage(Died, _))
    }
    sledId.sled.foreach(_.remove())
    println(s"sled killed: sledCount:${sledMap.size}")
  }

  /** update the score based on sled travel distance */
  private def updateScore(awards: Seq[Award]): Unit = {
    awards.foreach { award =>
      award match {
        case SledKill(winnerId, loserId) =>
          for {
            winnerConnectionId <- winnerId.connectionId
            winner <- winnerId.user
            loser <- loserId.user
          } {
            val points = loser.score / 2
            winner.addScore(points)
          }
        case Travel(sledId, distance)    =>
          for {
            connectionId <- sledId.connectionId
            user <- sledId.user
          } {
            val points = distance * Points.travel
            user.addScore(points)
          }
        case SnowballHit(winnerId)       =>
        case SledDied(loserId)           =>
          for {
            connectionId <- loserId.connectionId
            user <- loserId.user
          } {
            user.setScore((score: Double) => {
              Math.max(score / 2, 10)
            })
          }
      }
    }
  }

  /** check for collisions between the sled and trees or snowballs */
  private def checkCollisions(): Seq[SledKill] = {
    import snowy.collision.GameCollide.snowballTrees
    val awards = mutable.ListBuffer[SledKill]()

    def updateGlobalSleds(replace: Traversable[SledReplace]): Unit = {
      sleds = replace.foldLeft(sleds) { case (newSleds, SledReplace(oldSled, newSled)) =>
        newSleds.remove(oldSled).add(newSled)
      }
    }

    // collide snowballs with each sled
    // . update global snowballs to remove collisions after each iteration
    // . update local awards table from any sleds that were killed
    // . return the revised sleds after damage taken from snowballs
    val ballSleds: Set[SledReplace] =
    sleds.items.flatMap { sled =>
      collideBalls(sled, snowballs).map { case (newSled, newBalls, newAwards) =>
        snowballs = newBalls
        awards ++= newAwards
        SledReplace(sled, newSled)
      }
    }
    updateGlobalSleds(ballSleds)

    val treeSleds =
      sleds.items.flatMap { sled =>
        SledTree.collide(sled, trees).map { newSled =>
          SledReplace(sled, newSled)
        }
      }
    updateGlobalSleds(treeSleds)

    val sledSleds = SledSled.collide(sleds.items)
    updateGlobalSleds(sledSleds)

    snowballs = snowballs.removeMatchingItems(snowballTrees(_, trees))

    awards.toList
  }

  /** Collide a sled with a set of snowballs
    *
    * @return if there's a collision, returns:
    *         the damaged sled,
    *         the snowball set with the colliding ball removed,
    *         awards to the shooters if the sled was killed
    */
  private def collideBalls(sled: Sled, balls: Store[Snowball])
  : Option[(Sled, Store[Snowball], Traversable[SledKill])] = {
    SledSnowball.collide(sled, snowballs).map { case (newSled, newBalls, awards) =>
      val killAwards =
        if (newSled.health <= 0) {
          awards.map { winner => SledKill(winner.sledId, newSled.id) }
        } else {
          Nil
        }
      (newSled, newBalls, killAwards)
    }
  }

  /** Send the current score to the clients */
  private def sendScores(): Unit = {
    val scores = {
      val rawScores = users.values.map { user => Score(user.name, user.score) }.toSeq
      val sorted = rawScores.sortWith{(a,b) => a.score > b.score}
      sorted.take(10)
    }
    users.collect {
      case (id, user) if !user.robot && user.timeToSendScore(gameTime) =>
        user.scoreSent(gameTime)
        val scoreboard = Scoreboard(user.score, scores)
        sendMessage(scoreboard, id)
    }
  }


  /** Advance to the next game simulation state
    *
    * @return the time since the last time slice, in seconds
    */
  private def nextTimeSlice(): Double = {
    val currentTime = System.currentTimeMillis()
    val deltaSeconds = (currentTime - gameTime) / 1000.0
    gameTime = currentTime
    deltaSeconds
  }
}

case class SledReplace(oldSled: Sled, newSled: Sled)
