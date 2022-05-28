package actors

import actors.TubeLocationService.{CloseToTubeStation, CloseToTubeStationResponse, Command}
import akka.actor.typed.{ActorRef, Behavior, PostStop, Signal}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import location.Location

object TubeLocationService {
  sealed trait Command
  case class CloseToTubeStation(loc: Location)
  case class CloseToTubeStationResponse(isClose: Boolean)

  def apply(): Behavior[Command] = {
    Behaviors.setup(ctx => new TubeLocationService(ctx))
  }
}

class TubeLocationService(ctx: ActorContext[Command]) extends AbstractBehavior[Command](ctx) {

  val distance: Double = 1D/60D
  var count: Long = 0L
  var countNearTubeStation: Long = 0L

  override def onMessage(msg: Command): Behavior[Command] =
    msg match {
      case CloseToTubeStation(loc: Location) =>
        val actor = context.spawn(Behaviors.empty[CloseToTubeStationResponse], "tubeLocationResponseActor")
        count += 1
        actor ! CloseToTubeStationResponse(isCloseToTubeStation(loc))
        this
    }

  override def onSignal: PartialFunction[Signal, Behavior[Command]] = {
    case PostStop =>
      ctx.log.info(s"Stopping, processed $count locations ($countNearTubeStation were near a Tube Station)")
      this
  }

  private def isCloseToTubeStation(loc: Location): Boolean = loc match {
    case Location(long,lat) if Math.abs(Math.abs(lat) - 50.0D) < distance && Math.abs(long) < distance =>
      countNearTubeStation += 1
      true
    case _ =>
      false
  }
}
