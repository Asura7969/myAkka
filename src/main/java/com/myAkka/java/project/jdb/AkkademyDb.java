package com.myAkka.java.project.jdb;

import akka.actor.AbstractActor;
import akka.actor.Status;
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
                    map.put(message.key, message.value);
                    sender().tell(new Status.Success(message.key), self());
                })
                .match(GetRequest.class, message -> {
                    log.info("Received Get request: {}", message);
                    Object value = map.get(message.key);
                    Object response = (value != null)
                            ? value
                            : new Status.Failure(new KeyNotFoundException(message.key));
                    sender().tell(response, self());
                })
                .matchAny(o -> sender().tell(new Status.Failure(new ClassNotFoundException()), self()))
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
