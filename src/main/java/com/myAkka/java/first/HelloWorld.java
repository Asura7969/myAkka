package com.myAkka.java.first;

import akka.actor.*;

public class HelloWorld extends UntypedActor {

    @Override
    public void preStart() throws Exception {
        final ActorRef greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        greeter.tell(Greeter.Msg.GREET,getSelf());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Greeter.Msg.DONE) {
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }




    public static class Greeter extends UntypedActor {

        public static enum Msg {
            GREET,DONE
        }

        @Override
        public void onReceive(Object message) throws Throwable {
            if (message == Msg.GREET) {
                System.out.println("Hello World");
                Thread.sleep(1000);
                getSender().tell(Msg.DONE,getSelf());
            } else {
                unhandled(message);
            }
        }
    }


    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("HelloAkka");
        ActorRef a = system.actorOf(Props.create(HelloWorld.class), "helloWorld");
        System.out.println(a.path());


        akka.Main.main(new String[] { HelloWorld.class.getName() });
    }
}
