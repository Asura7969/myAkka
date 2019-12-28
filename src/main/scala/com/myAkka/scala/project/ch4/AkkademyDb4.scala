package com.myAkka.scala.project.ch4

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Status}
import akka.event.Logging

import scala.collection.mutable.HashMap

class AkkademyDb4 extends Actor{
  val map = new HashMap[String,Object]
  val log = Logging(context.system,this)

  override def receive: Receive = {
    case x:Connected =>
      sender() ! x

    case x:List[_] =>
      x.foreach{
        case SetRequest(key, value, senderRef) =>
          handleSetRequest(key,value,senderRef)
        case GetRequest(key, senderRef) =>
          handleGetRequest(key, senderRef)
      }

    case SetRequest(key, value, senderRef) =>
      handleSetRequest(key,value,senderRef)

    case GetRequest(key, senderRef) =>
      handleGetRequest(key, senderRef)

    case _ =>
      log.info("unknown message")
      sender() ! Status.Failure(new ClassNotFoundException)
  }

  def handleSetRequest(key: String, value:Object,senderRef: ActorRef): Unit ={
    log.info("received SetRequest - key: {} value: {}", key, value)
    map.put(key, value)
    senderRef ! Status.Success
  }

  def handleGetRequest(key: String, senderRef: ActorRef): Unit ={
    log.info("received GetRequest - key: {}", key)
    val response = map.get(key)
    response match {
      case Some(x) => senderRef ! x
      case None => senderRef ! Status.Failure(new KeyNotFoundException(key))
    }
  }
}

object Main{
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("akkademy")
    val helloActor = system.actorOf(Props[AkkademyDb4], name = "akkademy-db")
  }
}
