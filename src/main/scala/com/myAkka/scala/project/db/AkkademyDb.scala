package com.myAkka.project.db

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import com.myAkka.project.db.message._

import scala.collection.mutable

class AkkademyDb extends Actor{

  val map = new mutable.HashMap[String,Object]
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case SSetRequest(key, value) =>
      log.info("received SSetRequest - key:{} value:{}", key, value)
      map.put(key, value)
      sender ! Status.Success

    case SGetRequest(key) =>
      log.info("received SGetRequest - key: {}", key)
      val response = map.get(key)
      response match {
        case Some(x) => sender ! x
        case None => sender ! Status.Failure(SKeyNotFoundException(key))
      }

    case SDeleteRequest(key) =>
      log.info("received SDeleteRequest - key: {}", key)
      val response = map.remove(key)
      response match {
        case Some(x) => sender ! x
        case None => sender ! s"$key no exists"
      }

    case SSetIfNotExists(key,value) =>
      log.info("received SSetIfNotExists - key: {} value:{}", key,value)
      val option = map.get(key)
      if (option.isEmpty) {
        map.put(key, value)
        sender ! Status.Success
      } else {
        sender ! s"$key exists"
      }

    case o => Status.Failure(new ClassNotFoundException)
  }
}


object Main extends App {
  val system = ActorSystem("akkademy")
  system.actorOf(Props[AkkademyDb], name = "akkademy-db")
}