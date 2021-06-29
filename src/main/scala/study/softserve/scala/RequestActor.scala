package study.softserve.scala

import akka.actor.Actor
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.pattern.pipe
import akka.util.Timeout
import com.myprotos.myproto.CityWeather
import org.json4s.jackson.JsonMethods

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class RequestActor extends Actor {

  implicit val timeout: Timeout = Timeout(apiResponseTimeout seconds)

  import system.dispatcher

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
        .map(response => JsonMethods.parse(response)
          .camelizeKeys
          .extract[CityWeather])
        .pipeTo(sen)
    }
  }
}