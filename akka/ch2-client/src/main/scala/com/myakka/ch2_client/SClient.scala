package com.myakka.ch2_client

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.myakka.ch2.messages.{GetRequest, SetRequest}

import scala.concurrent.duration._

class SClient(remoteAddress: String){
  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("LocalSystem")
  private val remoteDb = system.actorSelection(s"akka.tcp://myakka@$remoteAddress/user/myakka")

  def set(key: String, value: Object) = {
    remoteDb ? SetRequest(key, value)
  }

  def get(key: String) = {
    remoteDb ? GetRequest(key)
  }
}

object Main{
  def main(args: Array[String]): Unit = {
    val client = new SClient("127.0.0.1:2552")
    client.set("key","value")

    client.get("key")
  }
}