package com.myakka.mailbox;

import akka.dispatch.ControlMessage;

/**
 * @author asura7969
 * @create 2021-07-29-22:08
 */
public class ControlMsg implements ControlMessage {

    private final String status;

    public ControlMsg(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
