package com.myakka.practice.yellowchickenserver.client

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.myakka.practice.yellowchickenserver.common.{ClientMessage, ServerMessage}
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

class CustomerActor extends Actor{

  //我们这里需要持有 Server 的 Ref
  var yellowChickenServerRef: ActorSelection = _

  //preStart , 在启动 Actor 之前会先运行，因此变量,初始化写入 preStart
  override def preStart(): Unit = {
    //println("preStart")
    //说明
    //1. 在 AKKA 的 Actor 模型中， 认为 每个 Actor 都是一个资源（角色），通过一个 Path 来定位一个 actor
    //2. path 的组成 akka.tcp://Server 的 actorfactory 名字@ServerIp:Server 的 port/user/ServerActor 名字
    yellowChickenServerRef = context.actorSelection("akka.tcp://Server@127.0.0.1:9999/user/YellowChickenServer")
  }

  override def receive: Receive = {
    case "start" =>
      println("客户端启动，可以咨询问题~~")
    case mes: String =>
      //将 mes 发送给 Server
      yellowChickenServerRef ! ClientMessage(mes)
    case ServerMessage(mes) =>
      println("收到小黄鸡客服回复的消息: " + mes)

  }
}

object CustomerActor extends App {

  //编写必要的配置信息
  val serverHost = "127.0.0.1"
  val serverPort = 9999
  val clientHost = "127.0.0.1"
  val clientPort = 10000

  val config = ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$clientHost
       |akka.remote.netty.tcp.port=$clientPort
       """.stripMargin)

  //创建 CustomerActor
  val clientActorSystem = ActorSystem("Client", config)

  val customerActorRef: ActorRef = clientActorSystem.actorOf(Props[CustomerActor], "CustomerActor")

  customerActorRef ! "start"

  println("可以咨询问题了")
  while (true) {
    val mes = StdIn.readLine()
    customerActorRef ! mes //先发给自己，然后让  CustomerActor 发

  }
}