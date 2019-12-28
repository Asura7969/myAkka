package com.myAkka.rpc

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

class Master extends Actor{

  println(s"Create master!")

  override def preStart(): Unit = {
    super.preStart()
    println(s"Master init...")
  }

  override def receive: Receive = {
    case "connect" =>
      println("一个 Worker 连接了...")
      sender ! "reply"
  }
}


object Master{
  def main(args: Array[String]): Unit = {

    val host = "127.0.0.1"
    val port = 9001
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
      """.stripMargin

    // 传入配置参数获取配置
    val config: Config = ConfigFactory.parseString(configStr)
    // 获取ActorSystem，指定名称和配置
    val masterSystem: ActorSystem = ActorSystem("MasterSystem",config)
    // 创建Actor
    val master = masterSystem.actorOf(Props(new Master),"master")
    // 自己给自己发送给消息
    master ! "connect"
//    masterSystem
  }
}