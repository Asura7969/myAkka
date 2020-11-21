package com.myakka.practice.kungfu

import akka.actor.{ActorRef, ActorSystem, Props}

object KungFuActor {

  def main(args: Array[String]): Unit = {
    val actorfactory = ActorSystem("actorFactor")
    val qiaoFenActor: ActorRef = actorfactory.actorOf(Props[QiaoFenActor],"QiaoFenActor")
    val huangFeiHongActor: ActorRef = actorfactory.actorOf(Props(new HuangFeiHongActor(qiaoFenActor)),"HuangFeiHongActor")

    huangFeiHongActor ! "start"
  }


}
