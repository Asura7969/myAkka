package com.myakka.future

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object FutureTest {
  def main(args: Array[String]): Unit = {
    for {
      a <- Future {println(s"${Thread.currentThread().getName} 1")
        Thread.sleep(1000)}
      b <- Future {println(s"${Thread.currentThread().getName} 2")
        Thread.sleep(300)}
      c <- Future {
        println(s"${Thread.currentThread().getName} 3")
        Thread.sleep(300)
      }
    } yield {
      println(s"${Thread.currentThread().getName} 4 ")
    }
  }
}