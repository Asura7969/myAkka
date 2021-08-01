package com.myakka.router;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author asura7969
 * @create 2021-08-01-15:11
 */
public class RouteeActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(String.class, s -> {
            log.info("self:{}  ->  msg:{}", getSelf(), s);
        }).build();
    }
}
