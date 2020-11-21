package com.myakka.ch3.messages

case class ParseArticle(url: String)
case class ParseHtmlArticle(url: String, htmlString: String)
case class HttpResponse(body: String)
case class ArticleBody(url: String, body: String)
case class SetRequest(key: String, value: Object)
case class GetRequest(key: String)