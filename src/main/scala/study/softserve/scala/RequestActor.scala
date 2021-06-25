package study.softserve.scala

import akka.actor.Actor
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.pattern.pipe
import akka.util.Timeout
import org.json4s._
import org.json4s.jackson.JsonMethods._
import study.softserve.scala.JSONParser.CityWeather

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class RequestActor extends Actor {

  import system.dispatcher

  implicit val timeout: Timeout = Timeout(apiResponseTimeout seconds)


  def receive: Receive = {
    case request => {

      val responseFuture = Http().singleRequest(
        HttpRequest(
          method = HttpMethods.GET,
          uri = s"https://api.openweathermap.org/data/2.5/weather?q=$request&appid=$openWeatherApiKey",
        )
      )

      val sen = sender()

      responseFuture
        .flatMap(_.entity.toStrict(2 seconds))
        .map(_.data.utf8String)
        .map(response => parse(response)
          .camelizeKeys
          .extract[CityWeather])
        .pipeTo(sen)
    }
  }
}