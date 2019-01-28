package com.akkademy.java;

import static org.junit.Assert.assertEquals;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.myAkka.java.project.message.AkkademyDb;
import com.myAkka.java.project.message.SetRequest;
import org.junit.Test;

public class AkkademyDbTest {

    ActorSystem system = ActorSystem.create();

    @Test
    public void itShouldPlaceKeyValueFromSetMessageIntoMap() {
        TestActorRef<AkkademyDb> actorRef =
                TestActorRef.create(system, Props.create(AkkademyDb.class));
        actorRef.tell(
                new SetRequest("key", "value"), ActorRef.noSender());
        AkkademyDb akkademyDb = actorRef.underlyingActor();
        assertEquals(akkademyDb.map.get("key"), "value");
    }
}
