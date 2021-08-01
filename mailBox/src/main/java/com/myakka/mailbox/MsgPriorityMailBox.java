package com.myakka.mailbox;

import akka.actor.ActorSystem;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedStablePriorityMailbox;
import com.typesafe.config.Config;


/**
 * @author asura7969
 * @create 2021-07-27-23:29
 */
public class MsgPriorityMailBox extends UnboundedStablePriorityMailbox {

    /**
     * 必须有该构造函数, 会反射调用
     * @param settings
     * @param config
     */
    public MsgPriorityMailBox(ActorSystem.Settings settings, Config config) {
        super(new PriorityGenerator() {
            @Override
            public int gen(Object message) {
                if (message.equals("张三")) {
                    return 0;
                } else if (message.equals("李四")) {
                    return 1;
                } else if (message.equals("王五")) {
                    return 2;
                }
                return 3;
            }
        });
    }
}
