package com.myakka.practice.cook

import akka.actor.{ActorRef, ActorSystem}

object Cafe extends App {
  import Kitchen._
  val cafeSystem = ActorSystem("cafe")

  val kitchen: ActorRef = cafeSystem.actorOf(kitchenProps,"kitchen")

  println(s"Calling chef at ${System.currentTimeMillis()}")
  kitchen ! "CookCook"
  println(s"Calling chef at ${System.currentTimeMillis()}")
  Thread.sleep(1000)
  println(s"Calling chef at ${System.currentTimeMillis()}")
  kitchen ! "CookCook"
  Thread.sleep(1000)
  kitchen ! "CookCook"
  Thread.sleep(1000)
  kitchen ! "CookCook"
  Thread.sleep(1000)
  kitchen ! "CookCook"

  Thread.sleep(1000 * 30)
  cafeSystem.terminate()
}
