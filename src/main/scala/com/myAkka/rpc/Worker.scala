package com.myAkka.rpc

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class Worker(val masterHost : String,val masterPort : Int) extends Actor{

  var master:ActorSelection = _

  println("Create Worker!")

  override def preStart(): Unit = {
    super.preStart()
    // 与master建立连接,拿到master引用
    master = context.actorSelection(s"akka.tcp://MasterSystem@$masterHost:$masterPort/user/master")
    // 向master发送信息
    master ! "connect"
  }

  override def receive: Receive = {
    case "reply" =>
      println("收到Master信息")
  }
}


object Worker{
  def main(args: Array[String]): Unit = {
    val host = args(0)
    val port = args(1).toInt
    val masterHost = args(2)
    val masterPort = args(3).toInt
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
      """.stripMargin
    //传入配置参数获取配置
    val config = ConfigFactory.parseString(configStr)
    //获取ActorSystem，指定名称和配置
    val workerSystem = ActorSystem("WorkerSystem",config)
    //创建Actor
    val actor = workerSystem.actorOf(Props(new Worker(masterHost,masterPort)),"worker")
//    workerSystem.awaitTermination()
  }

}
