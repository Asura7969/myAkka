package com.myakka.router.pool;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @author asura7969
 * @create 2021-08-01-15:39
 */
public class PoolDemo {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys");
        final ActorRef masterActor = system.actorOf(Props.create(MasterRouterActor.class), "masterActor");
        masterActor.tell("helloA", ActorRef.noSender());
        masterActor.tell("helloB", ActorRef.noSender());
        masterActor.tell("helloC", ActorRef.noSender());
    }
}
