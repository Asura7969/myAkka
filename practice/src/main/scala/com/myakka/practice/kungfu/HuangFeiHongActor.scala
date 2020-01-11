package com.myakka.practice.kungfu

import akka.actor.{Actor, ActorLogging, ActorRef}

/**
  * 黄飞鸿
  */
class HuangFeiHongActor(qiaoFenActor: ActorRef) extends Actor with ActorLogging{

  private var count = 1

  override def receive: Receive = {
    case "start" =>
      log.info("黄飞鸿上擂台...黄飞鸿开始挑衅乔峰...")
      Thread.sleep(1000L)
      qiaoFenActor ! "start"
    case Tiaoxin =>
      Thread.sleep(1000L)
      log.info(s"黄飞鸿:佛山无影脚...第 $count 脚")
      qiaoFenActor ! Skill
      count += 1
    case Skill =>
      if(count < 5){
        self ! Tiaoxin
      } else {
        qiaoFenActor ! Dead
        log.info("黄飞鸿扛不住了,吐了一口老血...退出了")
        context.stop(self)
        context.system.terminate()
      }

    case Resist =>
      self ! Tiaoxin
    case _ =>
      log.info("黄飞鸿:不知道对面在说啥,先打了再说...(给自己发送消息)")
      self ! Tiaoxin


  }
}
