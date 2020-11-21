package com.myakka.practice.yellowchickenserver.server

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.myakka.practice.yellowchickenserver.common.{ClientMessage, ServerMessage}
import com.typesafe.config.ConfigFactory

class YellowChickenServer extends Actor{


  override def receive: Receive = {
    case "start" =>
      println("小黄 开始监听程序，可以咨询问题~~")
    case ClientMessage(mes) =>
      //怎么匹配他的内容
      println("客户咨询的问题是" + mes)
      mes match {
        case "大数据学费" =>
          sender() ! ServerMessage("20000RMB")
        case "地址" =>
          sender() ! ServerMessage("北京昌平 XX 楼 111 号")
        case "课程" =>
          sender() ! ServerMessage("JavaEE Python 前端 大数据")
        case _ => {
          sender() ! ServerMessage("你说的啥子~~")
        }
      }
  }
}

object YellowChickenServer extends App{
  //创建 ActorSystem
  //因为这时，我们需要监听网络，所以使用如下方法创建工厂
  //Config 就是我们的网络配置 ip , port..
  //def apply(name: String, config: Config): ActorSystem = apply(name, Option(config), None, None)

  val host = "127.0.0.1" //ip4
  val port = 9999
  //Config 就是我们的网络配置 ip , port..

  val config = ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$host
       |akka.remote.netty.tcp.port=$port
       """.stripMargin)

  val serverActorSystem = ActorSystem("Server",config)

  val yellowChickenServerRef: ActorRef = serverActorSystem.actorOf(Props[YellowChickenServer],"YellowChickenServer")

  //akka.tcp://Server@127.0.0.1:9999  就是 Actor 路径
  yellowChickenServerRef ! "start"
}
