package com.myAkka.project.ch2

import akka.actor.{Actor, Props, Status}

class ScalaPongActor extends Actor{
  override def receive: Receive = {
    case "Ping" => sender() ! "Pong"
    case _ =>
      sender() ! Status.Failure(new Exception("unknown message"))
  }
}

object ScalaPongActor{
  def props(response:String):Props={
    Props(classOf[ScalaPongActor],response)
  }
}