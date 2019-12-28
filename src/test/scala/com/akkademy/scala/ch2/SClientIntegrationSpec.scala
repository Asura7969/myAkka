package com.akkademy.scala.ch2

import com.myAkka.project.db.SClient
import org.scalatest.{FunSpecLike, Matchers}
import scala.concurrent.duration._
import scala.concurrent.Await

class SClientIntegrationSpec extends FunSpecLike with Matchers{

  val client = new SClient("127.0.0.1:2552")

  describe("akkademyDbClient"){
    it("should set a value"){
      client.set("123",new Integer(123))
      val future = client.get("123")
      val value = Await.result(future,10 seconds)
      value should equal(123)
    }
  }

}
