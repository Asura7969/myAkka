package com.myakka.practice.cook3

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.myakka.practice.cook3.Cafe.Receipt

import scala.util.Random

object ReceiptPrinter {
  case class PrintReceipt(sendTo: ActorRef, receipt: Receipt)  //print command
  class PaperJamException extends Exception
  def props = Props[ReceiptPrinter]
}
class ReceiptPrinter extends Actor with ActorLogging {
  import ReceiptPrinter._
  var paperJammed: Boolean = false
  override def receive: Receive = {
    case PrintReceipt(customer, receipt) =>    //打印收据并发送给顾客
      if ((Random.nextInt(6) % 6) == 0) {
        log.info("Printer jammed paper ...")
        paperJammed = true
        throw new PaperJamException
      } else {
        log.info(s"Printing receipt $receipt and sending to ${customer.path.name}")
        customer ! receipt
      }
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info(s"Restarting ReceiptPrinter for ${reason.getMessage}...")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info(s"Started ReceiptPrinter for ${reason.getMessage}.")
    super.postRestart(reason)
  }

  override def postStop(): Unit = {
    log.info("Stopped ReceiptPrinter.")
    super.postStop()
  }
}
