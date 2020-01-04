package com.myakka.practice.cook2

import akka.actor.{Actor, ActorLogging, OneForOneStrategy, SupervisorStrategy}
import scala.concurrent.duration._

class Parent extends Actor with ActorLogging {

  def decider: PartialFunction[Throwable,SupervisorStrategy.Directive] = {
    case _: ChildActor.RndException => SupervisorStrategy.Restart
  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 30, withinTimeRange = 3 seconds) {
      decider.orElse(SupervisorStrategy.defaultDecider)
    }

  val childActor = context.actorOf(ChildActor.props,"childActor")

  override def receive: Receive = {
    //把所有收到的消息都转给childActor
    case msg@ _ => childActor ! msg
  }
}
