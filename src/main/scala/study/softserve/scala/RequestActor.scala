package study.softserve.scala

import akka.actor.Actor
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.pattern.pipe
import akka.util.Timeout
import org.json4s.jackson.JsonMethods
import weather.WeatherReply

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class RequestActor extends Actor {

  implicit val timeout: Timeout = Timeout(apiResponseTimeout seconds)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  private val appConfig = config.getConfig("application")
  private val apiKey = appConfig.getString("weather-api-key")

  def receive: Receive = {
    case request =>
      val responseFuture = Http().singleRequest(
        HttpRequest(
          method = HttpMethods.GET,
          uri = s"https://api.openweathermap.org/data/2.5/weather?q=$request&appid=$apiKey",
        ))

      responseFuture
        .flatMap(_.entity.toStrict(apiResponseTimeout seconds))
        .map(_.data.utf8String)
        .map(response => JsonMethods.parse(response)
          .camelizeKeys
          .extract[WeatherReply])
        .pipeTo(sender())
  }
}