package com.myakka.router.pool;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

/**
 * @author asura7969
 * @create 2021-08-01-15:33
 */
public class MasterRouterActor extends AbstractActor {

    ActorRef router = null;

    @Override
    public void preStart() throws Exception {
        router = getContext().actorOf(new RoundRobinPool(3).props(Props.create(TaskActor.class)), "taskActor");
        System.out.println("router:" + router);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Object.class, o->{
            router.tell(o, getSender());
        }).build();
    }

    public static class TaskActor extends AbstractActor {

        @Override
        public Receive createReceive() {
            return receiveBuilder().match(Object.class, o->{
                System.out.println(getSelf() + " -> " + o  + " -> " + getContext().parent());
            }).build();
        }
    }
}
