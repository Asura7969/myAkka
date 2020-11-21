package com.myakka.ch4_client

import akka.actor.FSM
import com.myakka.ch4
import com.myakka.ch4.Request
import com.myakka.ch4_client.StateContainerTypes.RequestQueue


sealed trait State
case object Disconnected extends State
case object Connected extends State
case object ConnectedAndPending extends State

case object Flush
case object ConnectedMsg

object StateContainerTypes {
  type RequestQueue = List[Request]
}

class FSMClientActor(address: String) extends FSM[State, RequestQueue]{
  private val remoteDb = context.system.actorSelection(address)

  startWith(Disconnected, List.empty[Request])

  when(Disconnected){
    case Event(_: ch4.Connected, container: RequestQueue) => //If we get back a ping from db, change state
      if (container.headOption.isEmpty)
        goto(Connected)
      else
        goto(ConnectedAndPending)
    case Event(x: Request, container: RequestQueue) =>
      remoteDb ! new ch4.Connected //Ping remote db to see if we're connected if not yet marked online.
      stay using (container :+ x) //Stash the msg
    case x =>
      println("uhh didn't quite get that: " + x)
      stay()
  }

  when (Connected) {
    case Event(x: Request, container: RequestQueue) =>
      goto(ConnectedAndPending) using(container :+ x)
  }

  when (ConnectedAndPending) {
    case Event(Flush, container) =>
      remoteDb ! container
      goto(Connected) using Nil
    case Event(x: Request, container: RequestQueue) =>
      stay using(container :+ x)
  }

  initialize()
}
