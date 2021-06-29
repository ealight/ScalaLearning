package study.softserve

import akka.actor.ActorSystem
import org.json4s.DefaultFormats

package object scala {
  implicit val system: ActorSystem = ActorSystem("Actor")
  implicit val formats: DefaultFormats.type = DefaultFormats

  val openWeatherApiKey: String = "932990025e60573ec55fb150d6ae8368"
  val cities = List("London", "Paris", "Kyiv", "Lviv", "Singapore", "Rome", "Beijing", "Seoul", "Milan")
  val apiResponseTimeout = 3
  val schedulerTimeout = 1
}
