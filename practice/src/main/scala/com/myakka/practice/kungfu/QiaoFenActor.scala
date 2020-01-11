package com.myakka.practice.kungfu

import akka.actor.{Actor, ActorLogging, Props}

/**
  * 乔峰
  */
class QiaoFenActor extends Actor with ActorLogging{

  // 挨打次数
  var state = 1
  // 出掌次数
  var count = 0L

  override def receive: Receive = {
    case "start" =>
      log.info(s"乔峰 上擂台...我让你 3 脚...")
      sender() ! Tiaoxin
    case Skill =>
      Thread.sleep(1000L)
      state += 1
      if(state > 3){
        count += 1
        log.info(s"乔峰:降龙十八掌...第 $count 掌")
        sender() ! Skill
      } else {
        sender() ! Resist
      }

    case Resist =>
      log.info("乔峰:你扛得住吗?")
      Thread.sleep(1000L)
      sender() ! Skill

    case Win =>
      log.info("乔峰:你个菜鸟!")
      context.stop(self)
    case Dead =>
      self ! Win
    case _ =>
      log.info("发送了未知信息!")

  }
}
