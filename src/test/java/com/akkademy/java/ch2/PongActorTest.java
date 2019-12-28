package com.akkademy.java.ch2;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import static akka.pattern.Patterns.ask;

import com.myAkka.java.project.ch2.JavaPongActor;
import org.junit.Ignore;
import org.junit.Test;
import scala.concurrent.Future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static scala.compat.java8.FutureConverters.*;

public class PongActorTest {

    ActorSystem system = ActorSystem.create();
    ActorRef actorRef =
            system.actorOf(Props.create(JavaPongActor.class), "BruceWillis");

    @Test
    public void shouldReplyToPingWithPong() throws Exception{
        final Future pingFuture = ask(actorRef, "Ping", 1000);
        final CompletionStage<String> cs = toJava(pingFuture);
        final CompletableFuture<String> jFuture = (CompletableFuture<String>) cs;
        assertEquals("Pong", jFuture.get(1000, TimeUnit.MILLISECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void shouldReplyToUnknownMessageWithFailure() throws Exception {
        Future sFuture = ask(actorRef, "unknown", 1000);
        final CompletionStage<String> cs = toJava(sFuture);
        final CompletableFuture<String> jFuture = (CompletableFuture<String>) cs;
        jFuture.get(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void shouldPrintToConsole() throws Exception{
        askPong("Ping").thenAccept(x -> System.out.println("replied with:" + x));
        Thread.sleep(100);
    }





    public CompletionStage<String> askPong(String message) {
        Future sFuture = ask(actorRef, message, 1000);
        final CompletionStage<String> cs = toJava(sFuture);
        return cs;
    }


}
