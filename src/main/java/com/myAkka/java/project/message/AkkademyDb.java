package com.myAkka.java.project.message;

import akka.actor.AbstractActor;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import java.util.HashMap;
import java.util.Map;

public class AkkademyDb extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
    public final Map<String, Object> map = new HashMap<>();

    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .match(SetRequest.class, message -> {
                    log.info("Received Set request: {}", message);
                    map.put(message.getKey(), message.getValue());
                })
                .matchAny(o -> log.info("received unknown message: {}", o))
                .build();
    }

//    @Override
//    public void onReceive(Object message) throws Throwable {
//        new ReceiveBuilder()
//                .match(SetRequest.class, msg->{
//                    log.info("Received Set request: {}", message);
//                    map.put(msg.getKey(), msg.getValue());
//                }).matchAny(o -> log.info("received unknown message: {}", o))
//                  .build();
//
//    }
}
