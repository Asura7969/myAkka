package com.myAkka.scala.project.ch3

import java.util.concurrent.TimeoutException

import akka.actor.Status.Failure
import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props, SupervisorStrategy}
import akka.util.Timeout

class TellDemoArticleParser(cacheActorPath: String,
                            httpClientActorPath: String,
                            articleParserActorPath: String,
                            implicit val timeout: Timeout) extends Actor{

  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(articleParserActorPath)

  implicit val ec = context.dispatcher

  override def receive: Receive = {
    case ParseArticle(uri) =>
      val extraActor = buildExtraActor(sender(), uri)

      cacheActor.tell(GetRequest(uri), extraActor)
      httpClientActor.tell(uri, extraActor)

      context.system.scheduler.scheduleOnce(timeout.duration, extraActor, "timeout")
  }

  // 监督策略
  override def supervisorStrategy: SupervisorStrategy = {
    super.supervisorStrategy
//    OneForOneStrategy(){
//      case BrokenPlateException => Resume
//    }
  }

  private def buildExtraActor(senderRef:ActorRef,uri:String):ActorRef = {
    return context.actorOf(Props(new Actor {
      override def receive: Receive = {
        case "timeout" =>
          senderRef ! Failure(new TimeoutException("timeout!"))
          context.stop(self)

        case HttpResponse(body) =>
          articleParserActor ! ParseHtmlArticle(uri,body)

        case body:String =>
          senderRef ! body
          context.stop(self)

        case ArticleBody(uri, body) =>
          cacheActor ! SetRequest(uri, body)
          senderRef ! body
          context.stop(self)

        case t =>
          println("ignoring msg: " + t.getClass)
      }
    }))
  }
}
