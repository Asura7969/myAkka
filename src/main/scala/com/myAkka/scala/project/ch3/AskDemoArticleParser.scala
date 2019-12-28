package com.myAkka.scala.project.ch3

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future

class AskDemoArticleParser(cacheActorPath:String,
                           httpClientActorPath:String,
                           acticleParserActorPath:String,
                           implicit val timeout:Timeout) extends Actor{

  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(acticleParserActorPath)
  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {
    case ParseArticle(uri) =>
      val senderRef: ActorRef = sender()
      val cacheResult = cacheActor ? GetRequest(uri)

      val result: Future[Any] = cacheResult.recoverWith{
        case _:Exception =>
          val fRawResult = httpClientActor ? uri
          fRawResult flatMap{
            case HttpResponse(rawArticle) =>
              articleParserActor ? ParseHtmlArticle(uri,rawArticle)

            case x =>
              Future.failed(new Exception("unknown message"))
          }
      }

      // 获取结果并将其通过管道发送给 Actor
      result onComplete{
        case scala.util.Success(x:String) =>
          println("cache result!")
          senderRef ! x

        case scala.util.Success(x:ArticleBody) =>
          cacheActor ! SetRequest(uri,x.body)
          senderRef ! x

        case scala.util.Failure(t) =>
          senderRef ! akka.actor.Status.Failure(t)

        case x =>
          println("unknown message!" + x)
      }

  }
}
