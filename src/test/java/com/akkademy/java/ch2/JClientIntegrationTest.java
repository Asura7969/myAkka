package com.akkademy.java.ch2;

import com.myAkka.java.project.jdb.JClient;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class JClientIntegrationTest {

    JClient jClient = new JClient("127.0.0.1:2552");

    @Test
    public void itShouldSetRecord() throws Exception{
        jClient.set("123",123);
        final Integer result = (Integer)((CompletableFuture) jClient.get("123")).get();
        assert result == 123;
    }

}
