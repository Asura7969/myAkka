package com.myAkka.hello

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * https://www.lightbend.com/activator/template/hello-akka#code/src/main/scala/HelloAkkaScala.scala
  */
class Greeter extends Actor{

  var greeting = ""

  override def receive: Receive = {
    case WhoToGreet(who) => greeting = s"hello, $who"
    case Greet           => sender() ! Greeting(greeting)
  }

}

object Greeter {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("helloAkka")
    val greeter = system.actorOf(Props[Greeter], "greeter")
//    greeter.tell(WhoToGreet("akka"), ActorRef.noSender)
  }
}
