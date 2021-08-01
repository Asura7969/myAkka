package com.myakka.ch1

import akka.actor.Actor
import akka.event.{Logging, LoggingAdapter}

import scala.collection.mutable
import scala.com.myakka.ch1.messages.SetRequest

class AkkademyDb extends Actor{

  val map = new mutable.HashMap[String,Object]()
  val log: LoggingAdapter = Logging(context.system,this)

  override def receive: Receive = {
    case SetRequest(key,value) =>
      log.info(s"received SetRequest - key :$key value :$value")
      map.put(key,value)
    case o =>
      log.info(s"received unknown message:$o")
  }
}
