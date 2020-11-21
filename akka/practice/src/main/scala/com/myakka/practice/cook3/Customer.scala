package com.myakka.practice.cook3

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import scala.concurrent.duration._

object Customer {
  sealed trait CustomerOrder
  case object OrderSpecial extends CustomerOrder
  case class OrderServed(rcpt: Cafe.Receipt) extends CustomerOrder
  case object ComebackLater extends CustomerOrder
  def props(cafe: ActorRef) = Props(new Customer(cafe))
}
class Customer(cafe: ActorRef) extends Actor with ActorLogging {

  import Customer._
  import context.dispatcher

  override def receive: Receive = {
    case OrderSpecial =>
      log.info("Customer place an order ...")
      cafe ! Cafe.PlaceOrder
    case OrderServed(rcpt) =>
      log.info(s"Customer says: Oh my! got my order ${rcpt.item} for ${rcpt.amt}")
    case ComebackLater =>
      log.info("Customer is not so happy! says: I will be back later!")
      context.system.scheduler.scheduleOnce(1 seconds){cafe ! Cafe.PlaceOrder}
  }
}
