package com.myAkka.scala.project.ch3

case class ParseArticle(url: String)
case class ParseHtmlArticle(url: String, htmlString: String)
case class HttpResponse(body: String)
case class ArticleBody(url: String, body: String)
case class GetRequest(uri:String)
case class SetRequest(uri:String,body:String)