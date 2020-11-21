package com.myakka.ch3

import akka.actor.Actor
import akka.pattern.ask
import akka.util.Timeout
import com.myakka.ch3.messages._

import scala.concurrent.Future

class AskDemoArticleParser (cacheActorPath: String,
                            httpClientActorPath: String,
                            acticleParserActorPath: String,
                            implicit val timeout: Timeout
                           ) extends Actor {
  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(acticleParserActorPath)
  import scala.concurrent.ExecutionContext.Implicits.global


  /**
    * Note there are 3 asks so this potentially creates 6 extra objects:
    * - 3 Promises
    * - 3 Extra actors
    * It's a bit simpler than the tell example.
    */
  override def receive: Receive = {
    case ParseArticle(uri) =>
      // 回调中需要使用发件人ref,参阅 Pipe 模式
      val senderRef = sender() //sender ref needed for use in callback (see Pipe pattern for better solution)

      // ask cache
      val cacheResult = cacheActor ? GetRequest(uri)

      val result = cacheResult.recoverWith { //if request fails, then ask the articleParseActor
        case _: Exception =>
          val fRawResult = httpClientActor ? uri

          fRawResult flatMap {
            case HttpResponse(rawArticle) =>
              articleParserActor ? ParseHtmlArticle(uri, rawArticle)
            case x =>
              Future.failed(new Exception("unknown response"))
          }
      }

      // take the result and pipe it back to the actor
      // (see Pipe pattern for improved implementation)
      result onComplete {
        case scala.util.Success(x: String) =>
          println("cached result!")
          senderRef ! x //cached result
        case scala.util.Success(x: ArticleBody) =>
          cacheActor ! SetRequest(uri, x.body)
          senderRef ! x
        case scala.util.Failure(t) =>
          senderRef ! akka.actor.Status.Failure(t)
        case x =>
          println("unknown message! " + x)
      }
  }
}
