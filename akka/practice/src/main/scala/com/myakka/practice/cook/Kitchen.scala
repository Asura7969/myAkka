package com.myakka.practice.cook

import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props, SupervisorStrategy}
import akka.pattern.{Backoff, BackoffSupervisor}

import scala.concurrent.duration._

object Kitchen{
  def kitchenProps: Props = {
    import Chef._
    val options = Backoff.onFailure(Props[Chef], "chef", 200 millis, 10 seconds, 0.0)
      .withSupervisorStrategy(OneForOneStrategy(maxNrOfRetries = 4, withinTimeRange = 30 seconds) {
        case _: ChefBusy =>
          SupervisorStrategy.Restart
      })
    BackoffSupervisor.props(options)
  }
}

class Kitchen extends Actor with ActorLogging{
  override def receive: Receive = {
    case x =>
      context.children foreach {child => child ! x}
  }
}


