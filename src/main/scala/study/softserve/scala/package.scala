package study.softserve

import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}
import org.json4s.DefaultFormats

package object scala {
  implicit val system: ActorSystem = ActorSystem("Actor")
  implicit val formats: DefaultFormats.type = DefaultFormats

  val config: Config = ConfigFactory.load()

  val cities = List("London", "Paris", "Kyiv", "Lviv", "Singapore", "Rome", "Beijing", "Seoul", "Milan")
  val apiResponseTimeout = 5
  val schedulerTimeout = 1
}
