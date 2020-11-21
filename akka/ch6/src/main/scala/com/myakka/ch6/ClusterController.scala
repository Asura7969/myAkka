package com.myakka.ch6

import akka.actor.Actor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{MemberEvent, UnreachableMember}
import akka.event.Logging

class ClusterController extends Actor{

  val log = Logging(context.system,this)
  val cluster = Cluster(context.system)

  override def preStart(){
    cluster.subscribe(self,classOf[MemberEvent],classOf[UnreachableMember])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {
    case x:MemberEvent =>
      // 该事件会在集群状态发生变化时发出通知
      log.info(s"MemberEvent: $x")
    case x: UnreachableMember =>
      // 该事件会在某个节点被标记为不可用时发出通知
      log.info(s"UnreachableMember: $x")
  }
}
