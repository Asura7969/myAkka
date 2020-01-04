package com.myakka.practice.cook3

import akka.actor._
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.duration._


object Cafe {
  sealed trait Coffee  //咖啡种类
  case object Original extends Coffee
  case object Espresso extends Coffee
  case object Cappuccino extends Coffee

  case class Receipt(item: String, amt: Double)

  sealed trait Routine
  case object PlaceOrder extends Routine
  case class Sold(receipt: Receipt) extends Routine

}

class Cafe extends Actor with ActorLogging {
  import Cafe._
  import Cashier._

  import context.dispatcher
  implicit val timeout = Timeout(1 seconds)

  var totalAmount: Double = 0.0

  val kitchen = context.actorOf(Kitchen.kitchenProps,"kitchen")
  //Chef可能重启，但path不变。必须直接用chef ? msg，否则经Kitchen转发无法获取正确的sender
  val chef: ActorSelection = context.actorSelection("/user/cafe/kitchen/chef")

  val cashier = context.actorOf(Cashier.props(kitchen),"cashier")

  var customer: ActorRef = _     //当前客户

  override def receive: Receive = {

    case Sold(rcpt) =>
      totalAmount += rcpt.amt
      log.info(s"Today's sales is up to $totalAmount")
      customer ! Customer.OrderServed(rcpt)   //send him the order
      if (totalAmount > 100.00) {
        log.info("Asking kichen to clean up ...")
        context.stop(kitchen)
      }
    case PlaceOrder =>
      customer = sender()     //send coffee to this customer
      (for {
        item <- (chef ? Chef.MakeSpecial).mapTo[Coffee]
        sales <- (cashier ? RingRegister(item,sender())).mapTo[Sold]
      } yield (Sold(sales.receipt))).mapTo[Sold]
        .recover {
          case _: AskTimeoutException => Customer.ComebackLater
        }.pipeTo(self)   //send receipt to be added to totalAmount

  }
}
