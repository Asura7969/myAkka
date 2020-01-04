package com.myakka.practice.cook3

import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props, SupervisorStrategy, Terminated}
import scala.concurrent.duration._

object Cashier {
  case class RingRegister(cup: Cafe.Coffee, customer: ActorRef)  //收款并出具收据

  def props(kitchen: ActorRef) = Props(classOf[Cashier],kitchen)
}

class Cashier(kitchen: ActorRef) extends Actor with ActorLogging {
  import Cashier._
  import ReceiptPrinter._

  context.watch(kitchen)   //监视厨房。如果打烊了就关门歇业
  val printer = context.actorOf(ReceiptPrinter.props,"printer")
  //打印机卡纸后重启策略
  def cashierDecider: PartialFunction[Throwable,SupervisorStrategy.Directive] = {
    case _: PaperJamException => SupervisorStrategy.Restart
  }
  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = 5 seconds){
      cashierDecider.orElse(SupervisorStrategy.defaultDecider)
    }

  val menu = Map[Cafe.Coffee,Double](Cafe.Original -> 5.50, Cafe.Cappuccino -> 12.95, Cafe.Espresso -> 11.80)

  override def receive: Receive = {
    case RingRegister(coffee, customer) => //收款并出具收据
      log.info(s"Producing receipt for a cup of ${coffee.toString}...")
      val amt = menu(coffee)    //计价
      val rcpt = Cafe.Receipt(coffee.toString,amt)
      printer ! PrintReceipt(customer,rcpt)  //打印收据。可能出现卡纸异常
      sender() ! Cafe.Sold(rcpt)  //通知Cafe销售成功  sender === Cafe
    case Terminated(_) =>
      log.info("Cashier says: Oh, kitchen is closed. Let's make the end of day!")
      context.system.terminate()   //厨房打烊，停止营业。
  }
}
