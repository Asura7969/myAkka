package com.akkademy.scala.ch2

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.myAkka.project.ch2.ScalaPongActor
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}

class ScalaAskExamplesTest extends FunSpecLike with Matchers{

  val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)
  val pongActor = system.actorOf(Props(classOf[ScalaPongActor]))

  describe("Pong actor"){
    it("should respond with Pong"){
      val future = pongActor ? "Pong"
      val result = Await.result(future.mapTo[String],1 seconds)
      assert(result == "Pong")
    }
    it("should fail on unknown message"){
      val future = pongActor ? "unknown"
      intercept[Exception]{
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }

  describe("future example"){
    import scala.concurrent.ExecutionContext.Implicits.global
    it("should print to console"){
      (pongActor ? "Ping").onSuccess({
        case x:String => println("replied with: " + x)
      })
      Thread.sleep(100)
    }


    // 转换操作
    it("should transform"){
      val f: Future[Char] = askPong("Ping").map(x => x.charAt(0))
      val c = Await.result(f, 1 second)
      c should equal('P')
    }

    /**
      * Sends "Ping". Gets back "Pong"
      * Sends "Ping" again when it gets "Pong"
      */
    it("should transform async"){
      val f: Future[String] = askPong("Ping").flatMap(x => {
        assert(x == "Pong")
        askPong("Ping")
      })
      val c = Await.result(f, 1 second)
      c should equal("Pong")
    }

    //doesn't actually test anything - demonstrates an effect. next test shows assertion.
    // 处理异常
    it("should effect on failure"){
      askPong("causeError").onFailure{
        case e: Exception => println("Got exception")
      }
    }

    /**
      * similar example to previous test, but w/ assertion
      */

    it("should effect on failure (with assertion)"){
      val res = Promise()
      askPong("causeError").onFailure{
        case e: Exception =>
          res.failure(new Exception("failed!"))
      }

      intercept[Exception]{
        Await.result(res.future, 1 second)
      }
    }

    // 从失败中恢复
    it("should recover on failure"){
      val f = askPong("causeError").recover({
        case t: Exception => "default"
      })

      val result = Await.result(f, 1 second)
      result should equal("default")
    }

    // 异步地从失败中恢复
    it("should recover on failure async"){
      val f = askPong("causeError").recoverWith({
        case t: Exception => askPong("Ping")
      })

      val result = Await.result(f, 1 second)
      result should equal("Pong")
    }

    it("should chain together multiple operations"){
      val f = askPong("Ping").flatMap(x => askPong("Ping" + x)).recover({
        case _: Exception => "There was an error"
      })

      val result = Await.result(f, 1 second)
      result should equal("There was an error")
    }

    it("should be handled with for comprehension"){
      val f1: Future[Int] = Future{4}
      val f2: Future[Int] = Future{5}

      val futureAddition =
        for{
          res1 <- f1
          res2 <- f2
        } yield res1 + res2
      val additionResult = Await.result(futureAddition, 1 second)
      assert(additionResult == 9)
    }

    it("should handle a list of futures"){
      val listOfFutures: List[Future[String]] = List("Pong", "Pong", "failure").map(x => askPong(x))
      val futureOfList: Future[List[String]] = Future.sequence(listOfFutures)
    }
  }



  def askPong(message:String):Future[String] = (pongActor ? message).mapTo[String]

}
