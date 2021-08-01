package com.myakka.mailbox;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author asura7969
 * @create 2021-07-27-22:39
 */
public class MailBoxDemo {

    public static void main(String[] args) throws Exception {

        final ActorSystem system = ActorSystem.create("my-mailbox");
        final ActorRef actorRef = system.actorOf(Props.create(MsgActorDemo.class)
                .withMailbox("msgprio-mailbox"), "mailboxActorDemo");

        Object[] message = {"王五", "张三", "10", "李四"};
        for (Object o : message) {
            actorRef.tell(o, ActorRef.noSender());
        }

        Thread.sleep(2000);
        System.out.println("----------------------------");

        // 指令优先消息
        final ActorSystem controlSys = ActorSystem.create("controlSys");
        final ActorRef controlAwareRef = controlSys.actorOf(
                Props.create(MsgActorDemo.class).withMailbox("control-aware-mailbox"), "controlAware");

        Object[] language = {"scala", "java", new ControlMsg("kill -9 7773"), "akka"};
        for (Object o : language) {
            controlAwareRef.tell(o, ActorRef.noSender());
        }
    }


    public static class MsgActorDemo extends AbstractActor {
        private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);
        @Override
        public Receive createReceive() {
            return receiveBuilder().match(String.class, s -> {
                log.info("\nmsg:{}", s);
            }).match(Object.class, o -> {
                System.out.println(o.toString());
            }).build();
        }
    }
}
