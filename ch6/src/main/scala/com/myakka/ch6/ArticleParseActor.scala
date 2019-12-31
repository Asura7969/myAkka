package com.myakka.ch6

import akka.actor.Actor

class ArticleParseActor extends Actor{
  override def receive: Receive = {
    case htmlString: String =>
      val body: String = ArticleParse(htmlString)
      println(s"body: $body")
      sender() ! body
    case _ =>
      println("msg!")
  }
}
object ArticleParse {
  def apply(html: String) : String =
    de.l3s.boilerpipe.extractors.ArticleExtractor.INSTANCE.getText(html)
}