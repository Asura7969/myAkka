package com.myakka.practice.cook3

import akka.actor.{ActorSystem, Props}
import com.myakka.practice.cook3.Customer.OrderSpecial

import scala.concurrent.duration._

object MyCafe extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  val cafeSys = ActorSystem("cafeSystem")
  val cafe = cafeSys.actorOf(Props[Cafe],"cafe")
  val customer = cafeSys.actorOf(Customer.props(cafe),"customer")

  cafeSys.scheduler.schedule(1 second, 1 second, customer, OrderSpecial)
}
