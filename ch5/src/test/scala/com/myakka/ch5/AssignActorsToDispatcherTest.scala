package com.myakka.ch5

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing._
import com.myakka.ch5.TestHelper.TestCameoActor
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{Await, Promise}

class AssignActorsToDispatcherTest extends FlatSpec with Matchers {
  val system = ActorSystem()

  "ActorsAssignedToDispatcher" should "do work concurrently" in {

    val p = Promise[String]()

    val actors: IndexedSeq[ActorRef] = (0 to 7).map(x => {
      system.actorOf(Props(classOf[ArticleParseActor]).
        withDispatcher("article-parsing-dispatcher"))
    })

    val workerRouter = system.actorOf(RoundRobinGroup(
      actors.map(x => x.path.toStringWithoutAddress).toList).props(),
      "workerRouter")

    val cameoActor: ActorRef =
      system.actorOf(Props(new TestCameoActor(p)))

    (0 to 2000).foreach(x => {
      workerRouter.tell(
        new ParseArticle(TestHelper.file)
        , cameoActor);
    })

    TestHelper.profile(() => Await.ready(p.future, 20 seconds), "ActorsAssignedToDispatcher")
  }

}
