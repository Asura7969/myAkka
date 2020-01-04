package com.myakka.practice.cook3

import akka.actor.{OneForOneStrategy, Props, SupervisorStrategy}
import akka.pattern.{Backoff, BackoffSupervisor}
import scala.concurrent.duration._

object Kitchen {
  //指定的异常处理策略
  val kitchenDecider: PartialFunction[Throwable, SupervisorStrategy.Directive] = {
    case _: Chef.ChefBusy => SupervisorStrategy.Restart
  }
  def kitchenProps: Props = {  //定义BackoffSupervisor strategy
    val option = Backoff.onFailure(Props[Chef],"chef",1 seconds, 5 seconds, 0.0)
      .withManualReset
      .withSupervisorStrategy {
        OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = 5 seconds) {
          kitchenDecider.orElse(SupervisorStrategy.defaultDecider)
        }
      }
    BackoffSupervisor.props(option)
  }
}
