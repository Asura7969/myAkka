package com.myAkka.scala.project.ch4

import akka.actor.{Actor, Stash}

class HotswapClientActor(address: String) extends Actor with Stash{

  private val remoteDb = context.actorSelection(address)

  override def receive: Receive = {
    case x:Request =>
      remoteDb ! new Connected
      stash()

    case _:Connected =>
      unstashAll()
      context.become(online)
  }

  def online: Receive = {
    case x: Disconnected =>
      context.unbecome()
    case x: Request =>
      remoteDb forward x
  }
}

class Disconnected
