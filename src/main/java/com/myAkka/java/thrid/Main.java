package com.myAkka.java.thrid;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;

public class Main {

  public static void main(String[] args) {
    // =================================akka生命周期=================================
//    ActorSystem system = ActorSystem.create("Hello", ConfigFactory.load("akka.config"));
//    ActorRef myWork = system.actorOf(Props.create(MyWork.class), "MyWork");
//    ActorRef watchActor = system.actorOf(Props.create(WatchActor.class, myWork), "WatchActor");
//
//    myWork.tell(MyWork.Msg.WORKING, ActorRef.noSender());
//    myWork.tell(MyWork.Msg.DONE, ActorRef.noSender());
//
//    // 中断myWork
//    myWork.tell(PoisonPill.getInstance(), ActorRef.noSender());

    // =================================akka监督策略=================================
    ActorSystem sys = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
    ActorRef superVisor = sys.actorOf(Props.create(SuperVisor.class), "SuperVisor");
    superVisor.tell(Props.create(RestartActor.class), ActorRef.noSender());

    ActorSelection actorSelection = sys.actorSelection("akka://strategy/user/SuperVisor/restartActor");//这是akka的路径。restartActor是在SuperVisor中创建的。

    for(int i = 0 ; i < 100 ; i ++){
      actorSelection.tell(RestartActor.Msg.RESTART, ActorRef.noSender());
    }
  }
}