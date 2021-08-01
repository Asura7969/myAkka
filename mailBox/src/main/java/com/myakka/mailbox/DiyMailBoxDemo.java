package com.myakka.mailbox;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.Envelope;
import akka.dispatch.MailboxType;
import akka.dispatch.MessageQueue;
import akka.dispatch.ProducesMessageQueue;
import com.typesafe.config.Config;
import scala.Option;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author asura7969
 * @create 2021-07-29-22:30
 */
public class DiyMailBoxDemo {

    public static void main(String[] args) {

    }


    public static class BusinessMailBoxType implements MailboxType,
            ProducesMessageQueue<BusinessMsgQueue> {

        public BusinessMailBoxType(ActorSystem.Settings settings, Config config) {
        }

        @Override
        public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
            return new BusinessMsgQueue();
        }
    }



    public static class BusinessMsgQueue implements MessageQueue {

        private final Queue<Envelope> queue = new ConcurrentLinkedDeque<>();
        @Override
        public void enqueue(ActorRef receiver, Envelope e) {
            queue.offer(e);
        }

        @Override
        public Envelope dequeue() {
            return queue.poll();
        }

        @Override
        public int numberOfMessages() {
            return queue.size();
        }

        @Override
        public boolean hasMessages() {
            return !queue.isEmpty();
        }

        @Override
        public void cleanUp(ActorRef owner, MessageQueue deadLetters) {
            for (Envelope e : queue) {
                deadLetters.enqueue(owner, e);
            }
        }
    }
}
