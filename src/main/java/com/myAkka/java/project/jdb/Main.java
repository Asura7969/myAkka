package com.myAkka.java.project.jdb;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("akkademy");
        ActorRef actor = system.actorOf(Props.create(AkkademyDb.class), "akkademy-db");
    }
}
