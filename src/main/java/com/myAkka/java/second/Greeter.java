package com.myAkka.java.second;

import akka.actor.UntypedActor;

public class Greeter extends UntypedActor {
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Message) {
            System.out.println("Greeter 收到的数据为:" + message.toString());
            getSender().tell("Greeter 完成工作!",getSelf());
        } else {
            unhandled(message);
        }

    }
}
