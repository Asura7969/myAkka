package com.myAkka

package object hello {

  case object Greet
  case class WhoToGreet(who: String)
  case class Greeting(message: String)

}
