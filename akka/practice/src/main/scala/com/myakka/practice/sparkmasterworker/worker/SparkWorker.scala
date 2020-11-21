package com.myakka.practice.sparkmasterworker.worker

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.myakka.practice.sparkmasterworker.common.{HeartBeat, RegisterWorkerInfo, RegisteredWorkerInfo, SendHeartBeat}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

class SparkWorker(masterHost:String,masterPort:Int) extends Actor{
  var masterProxy: ActorSelection = _
  val id = UUID.randomUUID().toString

  override def preStart(): Unit = {
    masterProxy = context.actorSelection(s"akka.tcp://SparkMaster@$masterHost:$masterPort/user/SparkMaster01")
  }
  override def receive = {
    case "start" =>
      println("spark worker 启动..")
      //发出注册的请求
      masterProxy ! RegisterWorkerInfo(id, 8, 8 * 1024)
    case RegisteredWorkerInfo =>
      println(s"收到 master 回复消息 workerid= $id 注册成功")
      //启动一个定时器.
      import context.dispatcher
      //说明
      //1.schedule 创建一个定时器
      //2.0 millis, 延时多久才执行, 0 表示不延时，立即执行
      //3. 3000 millis 表示每隔多长时间执行 3 秒
      //4. self 给自己发送 消息
      //5. SendHeartBeat 消息
      context.system.scheduler.schedule(0 millis, 3000 millis, self, SendHeartBeat)

    case SendHeartBeat =>
      println(s"workerid= $id 发出心跳~")
      masterProxy ! HeartBeat(id)
  }
}

object SparkWorker extends App{
  val (masterHost,masterPort,workerHost,workerPort) =
    ("127.0.0.1",10000,"127.0.0.1",10001)
  val config = ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$workerHost
       |akka.remote.netty.tcp.port=$workerPort
       """.stripMargin)

  val sparkWorkerActorSystem = ActorSystem("SparkWorker",config)

  val sparkWorkerActorRef: ActorRef = sparkWorkerActorSystem.actorOf(Props(new SparkWorker(masterHost, masterPort)), "SparkWorker-01")

  sparkWorkerActorRef ! "start"

}