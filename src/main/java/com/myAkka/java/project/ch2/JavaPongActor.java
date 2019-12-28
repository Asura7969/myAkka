package com.myAkka.java.project.ch2;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;

public class JavaPongActor extends AbstractActor{
    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .matchEquals("Ping",s -> sender().tell("Pong", ActorRef.noSender()))
                .match(String.class,s -> System.out.println("It's a string: "+ s))
                .matchAny(x -> sender().tell(new Status.Failure(new Exception("unknown message")),self()))
                .build();
    }

    public static Props props(String response){
        return Props.create(JavaPongActor.class,response);
    }
}
