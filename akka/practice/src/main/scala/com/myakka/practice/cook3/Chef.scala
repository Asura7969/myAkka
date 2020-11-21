package com.myakka.practice.cook3

import akka.actor.{Actor, ActorLogging}
import akka.pattern.BackoffSupervisor
import com.myakka.practice.cook3.Chef.{ChefBusy, MakeSpecial}

import scala.util.Random

class Chef extends Actor with ActorLogging{

  log.info("Chef says: I am ready to work ...")

  //内部状态
  var currentSpecial: Cafe.Coffee = Cafe.Original
  var chefBusy: Boolean = false

  val specials = Map(0 -> Cafe.Original,1 -> Cafe.Espresso, 2 -> Cafe.Cappuccino)

  override def receive: Receive = {
    case MakeSpecial => {
      if ((Random.nextInt(6) % 6) == 0) {  //任意产生异常 2/6
        log.info("Chef is busy ...")
        chefBusy = true
        throw new ChefBusy("Busy!")
      }
      else {
        currentSpecial = randomSpecial     //选出当前特饮
        log.info(s"Chef says: Current special is ${currentSpecial.toString}.")
        sender() ! currentSpecial
      }
    }
  }

  def randomSpecial = specials(Random.nextInt(specials.size)) //选出当前特饮

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info(s"Restarting Chef for ${reason.getMessage}...")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info(s"Restarted Chef for ${reason.getMessage}.")
    context.parent ! BackoffSupervisor.Reset

    super.postRestart(reason)
  }

  override def postStop(): Unit = {
    log.info("Stopped Chef.")
    super.postStop()
  }

}

object Chef{
  sealed trait Order
  case object MakeSpecial extends Order
  class ChefBusy(msg:String) extends Exception(msg)

}