package com.myakka.ch5

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.{BalancingPool, RoundRobinGroup}
import com.myakka.ch5.TestHelper.TestCameoActor
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{Await, Promise}

class BalancingPoolSpec extends FlatSpec with Matchers {
  val system = ActorSystem()

  "BalancingPool" should "do work concurrently" in {
    val p = Promise[String]()

    val workerRouter = system.actorOf(BalancingPool(8).props(Props(classOf[ArticleParseActor])),
      "balancing-pool-router")

    val cameoActor: ActorRef =
      system.actorOf(Props(new TestCameoActor(p)))

    (0 to 2000).foreach(x => {
      workerRouter.tell(
        new ParseArticle(TestHelper.file)
        , cameoActor)
    })

    TestHelper.profile(() => Await.ready(p.future, 20 seconds), "ActorsInBalacingPool")
  }
}
