package com.myAkka.java.project.ch2;

import java.util.concurrent.CompletableFuture;

public class JavaFuture {

    public static void main(String[] args) {

        CompletableFuture<String> usernameFuture = getUsernameFromDatabaseAsync();
        usernameFuture.thenRun(() -> {
            System.out.println("username");
        });

        usernameFuture.whenCompleteAsync((v,e) -> {
            System.out.println(v);
            System.out.println(e);
        });
    }


    public static CompletableFuture<String> getUsernameFromDatabaseAsync(){
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            //长时间的计算任务
            return "·00";
        });
        return future;
    }
}
