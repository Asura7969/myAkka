package com.myakka.ch6

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.client.ClusterClientReceptionist
//import akka.contrib.pattern.ClusterReceptionistExtension
import akka.routing.BalancingPool

object MyAkkaMain extends App {

  val system = ActorSystem("Akkademy")
  val clusterController: ActorRef = system.actorOf(Props[ClusterController],"clusterController")
  val workers: ActorRef = system.actorOf(BalancingPool(5).props(Props[ArticleParseActor]), "workers")

  ClusterClientReceptionist(system).registerService(workers)
}
