import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorSystem, Behavior}

object DriverSystem {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new DriverSystem(context))
}

class DriverSystem(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "start" =>
        val firstRef = context.spawn(PrintMyActorRefActor(), "first-actor")
        println(s"First: $firstRef")
        firstRef ! "printit"
        this
    }
}

object GuardianActor extends App {
  val system = ActorSystem(DriverSystem(), "driverSystem")
  system ! "start"
}
