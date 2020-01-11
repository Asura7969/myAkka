package com.myakka.practice.sparkmasterworker.master

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.myakka.practice.sparkmasterworker.common._
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.collection.mutable

class SparkMaster extends Actor{

  //定义一个 hashMap,存放所有的 workers 信息
  val workers = mutable.HashMap[String, WorkerInfo]()

  override def receive = {
    case "start" =>
      println("spark master 启动，在 10000 监听..")
      self ! StartTimeOutWorker

    case RegisterWorkerInfo(id, cpu, ram) =>
      //注册
      //先判断是否已经有 id
      if (!workers.contains(id)) {
        //创建 WorkerInfo
        val workerInfo = new WorkerInfo(id, cpu, ram)
        workers += (id -> workerInfo)
        //workers += ((id,workerInfo))
        //回复成功!
        sender() ! RegisteredWorkerInfo
        println(s"workerid= $id 完成注册~")
      }
    case HeartBeat(id) =>
      //更新 id 对应的 worker 的心跳
      if (workers.contains(id)) {
        workers(id).lastHeartBeatTime = System.currentTimeMillis()
        println(s"workerid=$id 更新心跳成功~")
      }

    case StartTimeOutWorker =>
      //启动定时器
      import context.dispatcher
      context.system.scheduler.schedule(0 millis, 10000 millis, self, RemoveTimeOutWorker)

    case RemoveTimeOutWorker =>
      //定时清理超时 6s 的 worker,scala
      //获取当前的时间
      val currentTime = System.currentTimeMillis()
      val workersInfo = workers.values //获取到所有注册的 worker 信息
      //先将超时的一次性过滤出来，然后对过滤到的集合一次性删除
      workersInfo.filter(
        currentTime - _.lastHeartBeatTime > 6000
      ).foreach(workerInfo=>{
        workers.remove(workerInfo.id)
      })
      printf("当前有%d 个 worker 存活\n", workers.size)
  }
}


object SparkMaster extends App {

  val masterHost = "127.0.0.1"
  val masterPort = 10000

  val config = ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$masterHost
       |akka.remote.netty.tcp.port=$masterPort
       """.stripMargin)

  //创建 ActorSystem
  // "SparkMaster" actorFactory 名字，程序指定
  val sparkMasterActorSystem = ActorSystem("SparkMaster", config)
  //创建 SparkMaster 和 引用
  val sparkMaster01Ref: ActorRef = sparkMasterActorSystem.actorOf(Props[SparkMaster], "SparkMaster01")
  sparkMaster01Ref ! "start"
}