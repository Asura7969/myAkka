package com.myakka.build

/**
 * Build模式
 */
case class Car(carBody:String = "black", tyre:String = "blue")

object Main extends App {
  var car = Car()
  car.copy("白色", "xxx")
}