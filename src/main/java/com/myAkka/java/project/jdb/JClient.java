package com.myAkka.java.project.jdb;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

public class JClient {

    private final ActorSystem system = ActorSystem.create("LocalSystem");
    private final ActorSelection remoteDb;

    public JClient(String remoteAddress){
        remoteDb = system.actorSelection("akka.tcp://akkademy@" + remoteAddress + "/user/akkademy-db");
    }

    public CompletionStage set(String key, Object value) {
        return toJava(ask(remoteDb, new SetRequest(key, value), 2000));
    }

    public CompletionStage<Object> get(String key){
        return toJava(ask(remoteDb, new GetRequest(key), 2000));
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        JClient jClient = new JClient("127.0.0.1:2552");
        jClient.set("123",123);
        final Integer result = (Integer)((CompletableFuture) jClient.get("123")).get();
        System.out.println(result);
    }
}
