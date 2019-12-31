package com.myakka.ch1

import akka.actor.Actor
import akka.event.Logging
import com.myakka.ch1.messages.SetRequest

import scala.collection.mutable

class AkkademyDb extends Actor{

  val map = new mutable.HashMap[String,Object]()
  val log = Logging(context.system,this)

  override def receive: Receive = {
    case SetRequest(key,value) =>
      log.info(s"received SetRequest - key :$key value :$value")
      map.put(key,value)
    case o =>
      log.info(s"received unknown message:$o")
  }
}
