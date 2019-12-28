package com.myAkka.project.db.message

case class SSetRequest(key:String, value:Object)
case class SGetRequest(key: String)
case class SDeleteRequest(key:String)
case class SSetIfNotExists(key:String, value:Object)
case class SKeyNotFoundException(key: String) extends Exception
