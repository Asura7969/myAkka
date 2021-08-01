package com.myakka.dispatcher;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * PinnedDispatcher:
 *      为每个Actor提供只有一个线程的线程池,该线程池为该actor独有
 * Dispatcher:
 *      会让多个Actor绑定到一个线程池,当任务量很大时,会占用大量的线程去执行他们, 而新的任务会等待空闲的线程
 */

/**
 * @author asura7969
 * @create 2021-07-26-23:38
 */
public class DispatcherDemo {

    public static void main(String[] args) {
//        dispatcher();
        pinnedDispatcher();
    }

    public static void pinnedDispatcher() {
        final Config config  = ConfigFactory.load();
        // withOnlyPath: 获取到 myapp1 下面 key 为 'my-pinned-dispatcher' 的资源对象（只获取子集）
        // withFallback: 加 conf里面的其他配置项
        System.out.println(config.getConfig("myapp1").withOnlyPath("my-pinned-dispatcher"));
        final ActorSystem system = ActorSystem.create("MyApp1",
                config.getConfig("myapp1").withOnlyPath("my-pinned-dispatcher").withFallback(config));

        createActor(system, "my-pinned-dispatcher");
    }

    public static void dispatcher() {
        final ActorSystem system = ActorSystem.create("my-dispatcher");
        // 获取 system 的配置
//        System.out.println(system.settings());
        createActor(system, "my-forkjoin-dispatcher");

    }

    public static void createActor(ActorSystem system, String dispatcherName) {
        for (int i = 0; i < 20; i++) {
            final ActorRef actorRef = system.actorOf(Props.create(ActorDemo.class)
                    .withDispatcher(dispatcherName), "actorDemo" + i);
            actorRef.tell("" + i, ActorRef.noSender());
        }
    }


    public static class ActorDemo extends AbstractActor {
        private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);
        @Override
        public Receive createReceive() {
            return receiveBuilder().match(String.class, s -> {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName());
                log.info("\nmsg:{}\nsender:{}\nself:{}", s, getSender(), getSelf());
            }).build();
        }
    }
}
