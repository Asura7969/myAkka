package com.myakka.practice.cook2

import akka.actor.{Actor, ActorLogging, Props}
import com.myakka.practice.cook2.ChildActor.RndException

import scala.util.Random

class ChildActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case msg: String => {
      //任意产生一些RndExcption
      if (Random.nextBoolean())
        throw new RndException("Any Exception!")
      else
        log.info(s"Processed message: $msg !!!")
    }
  }

  override def preStart(): Unit = {
    log.info("ChildActor Started.")
    super.preStart()
  }

  //在重启时preRestart是在原来的Actor实例上调用preRestart的
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info(s"Restarting ChildActor for ${reason.getMessage}...")
    message match {
      case Some(msg) =>
        log.info(s"Exception message: ${msg.toString}")
        //把异常消息再摆放到信箱最后
        self ! msg
      case None =>
    }
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    log.info(s"Restarted ChildActor for ${reason.getMessage}...")
  }

  override def postStop(): Unit = {
    log.info(s"Stopped ChildActor.")
    super.postStop()
  }
}

object ChildActor{
  class RndException(msg: String) extends Exception(msg)
  def props = Props[ChildActor]
}
