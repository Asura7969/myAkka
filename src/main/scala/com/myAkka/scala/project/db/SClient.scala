package com.myAkka.project.db

import akka.actor.ActorSystem
import akka.pattern.ask

import scala.concurrent.duration._
import akka.util.Timeout
import com.myAkka.project.db.message.{SDeleteRequest, SGetRequest, SSetIfNotExists, SSetRequest}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Await
class SClient(remoteAddress: String) {
  private implicit val timeout = Timeout(2 seconds)
  lazy val conf: Config = {
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "127.0.0.1"
         |akka.remote.netty.tcp.port = "9009"
      """.stripMargin

    // 传入配置参数获取配置
    ConfigFactory.parseString(configStr)
  }
  private implicit val system = ActorSystem("LocalSystem",conf)

  private val remoteDb = system.actorSelection(s"akka.tcp://akkademy@$remoteAddress/user/akkademy-db")

  def set(key:String,value:Object): Unit ={
    remoteDb ? SSetRequest(key,value)
  }

  def get(key: String) = {
    remoteDb ? SGetRequest(key)
  }

  def delete(key:String) = {
    remoteDb ? SDeleteRequest(key)
  }

  def setIfNotExists(key:String,value:Object) ={
    remoteDb ? SSetIfNotExists(key,value)
  }

}

object SClient{
  def main(args: Array[String]): Unit = {
     val client = new SClient("127.0.0.1:2552")
    client.set("123",new Integer(123))
    val future = client.get("123")

    val value = Await.result(future,2 seconds)
    println(value)

    val setIfFuture = client.setIfNotExists("123",new Integer(321))
    val setIfFutureValue = Await.result(setIfFuture,2 seconds)
    println(setIfFutureValue)

    val deleteFuture = client.delete("123")
    val deleteValue = Await.result(deleteFuture,2 seconds)
    println(deleteValue)
  }
}
