package com.myakka.ch4

import akka.actor._
import akka.event.Logging
import scala.collection.mutable.HashMap

class AkkademyDb  extends Actor {
  val map = new HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive = {
    case x: Connected =>
      sender() ! x
    case x: List[_] =>
      x.foreach{
        case SetRequest(key, value, senderRef) =>
          handleSetRequest(key, value, senderRef)
        case GetRequest(key, senderRef) =>
          handleGetRequest(key, senderRef)
      }
    case SetRequest(key, value, senderRef) =>
      handleSetRequest(key, value, senderRef)
    case GetRequest(key, senderRef) =>
      handleGetRequest(key, senderRef)
    case o =>
      log.info("unknown message")
      sender() ! Status.Failure(new ClassNotFoundException)
  }

  def handleSetRequest(key: String, value: Object, senderRef: ActorRef): Unit = {
    log.info("received SetRequest - key: {} value: {}", key, value)
    map.put(key, value)
    senderRef ! Status.Success
  }

  def handleGetRequest(key: String, senderRef: ActorRef): Unit = {
    log.info("received GetRequest - key: {}", key)
    val response: Option[Object] = map.get(key)
    response match {
      case Some(x) => senderRef ! x
      case None => senderRef ! Status.Failure(new KeyNotFoundException(key))
    }
  }
}

object Main extends App {
  val system = ActorSystem("myakka")
  val helloActor = system.actorOf(Props[AkkademyDb], name = "myakka")
}

trait Request
case class SetRequest(key: String, value: Object, sender: ActorRef = ActorRef.noSender) extends Request
case class GetRequest(key: String, sender: ActorRef = ActorRef.noSender) extends Request

case class KeyNotFoundException(key: String) extends Exception

case class Connected() //Used as a ping