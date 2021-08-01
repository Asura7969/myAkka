package com.myakka.collection

import scala.io.Source
import scala.xml._

/**
 * @author asura7969
 * @create 2021-05-17-22:56
 */
object ParallelTest {

  def main(args: Array[String]): Unit = {
    timeSample(cities => cities map getWeatherData)

    timeSample(cities => (cities.par map getWeatherData).toList)

  }

  def getWeatherData(city: String) = {
//    val response = Source.fromURL(
//      s"https://github.com/ReactivePlatform/Pragmatic-Scala-StaticResources/tree/master/src/main/resources/weathers/" +
//        s"$city.xml")

    val response = Source.fromFile(s"E:\\IdeaProjects\\myAkka\\design\\src\\main\\resources\\weathers\\$city.xml")

    println(response.mkString)
    val xmlResponse = XML.loadString(response.mkString)
    val cityName = (xmlResponse \\ "city" \ "@name").text
    val temperature = (xmlResponse \\ "temperature" \ "@value").text
    val condition = (xmlResponse \\ "weather" \ "@value").text
    (cityName, temperature, condition)
  }

  def printWeatherData(weatherData: (String, String, String)): Unit = {
    val (cityName, temperature, condition) = weatherData
    println(f"$cityName%-15s $temperature%-6s $condition")
  }

  def timeSample(getData: List[String] => List[(String, String, String)]): Unit = {
    val cities = List("Bangalore,india",
      "Berlin,germany",
      "Boston,us",
      "Brussels,belgium",
      "Chicago,us",
      "Houston,us",
      "Krakow,poland",
      "London,uk",
      "Minneapolis,us",
      "Oslo,norway",
      "Reykjavik,iceland",
      "Rome,italy",
      "Stockholm,sweden",
      "Sydney,australia",
      "Tromso,norway")
    val start = System.nanoTime
    getData(cities) sortBy{_._1} foreach printWeatherData
    val end = System.nanoTime
    println(s"Time taken: ${(end -start)/1.0e9} s")
  }

}
