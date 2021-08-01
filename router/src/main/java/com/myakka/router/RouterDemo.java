package com.myakka.router;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author asura7969
 * @create 2021-08-01-15:14
 */
public class RouterDemo {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("sys");
        final ActorRef routerTaskActor = system.actorOf(Props.create(RouterTaskActor.class), "routerTaskActor");
        routerTaskActor.tell("helloA", ActorRef.noSender());
        routerTaskActor.tell("helloB", ActorRef.noSender());
        routerTaskActor.tell("helloC", ActorRef.noSender());
    }




    public static class RouterTaskActor extends AbstractActor {

        private Router router;

        @Override
        public void preStart() throws Exception {
            List<Routee> listRoutee = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                final ActorRef ref = getContext().actorOf(Props.create(RouteeActor.class), "routeActor" + i);
                listRoutee.add(new ActorRefRoutee(ref));
            }
//            router = new Router(new RoundRobinRoutingLogic(), listRoutee);
            router = new Router(new BroadcastRoutingLogic(), listRoutee);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().match(Object.class, o -> {
                router.route(o, getSender());
            }).build();
        }
    }
}
