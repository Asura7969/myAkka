package com.myAkka.java.second;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.alibaba.fastjson.JSONObject;
import com.typesafe.config.ConfigFactory;

import java.util.Arrays;

/**
 * 不可变对象
 */
public class HelloWorld extends UntypedActor {

    ActorRef greeter;

    /**
     * 初始化时候调用
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        System.out.println("Greeter actor path:" + greeter.path());
        greeter.tell(new Message(2, Arrays.asList("2","dsf")),getSelf());
    }

    public void onReceive(Object message) throws Throwable {
        System.out.println("HelloWorld收到的数据为：" + message);

    }

    public static void main(String[] args) {
//        akka.Main.main(new String[] {
//                HelloWorld.class.getName()
//        });

        ActorSystem system = ActorSystem.create("Hello", ConfigFactory.load("akka.config"));
        ActorRef a = system.actorOf(Props.create(HelloWorld.class), "helloWorld");
        System.out.println(a.path());
    }
}
