package study.softserve.scala

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import study.softserve.scala.JSONParser.CityWeather

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object Application {

  import system.dispatcher

  def main(args: Array[String]): Unit = {
    implicit val timeout: Timeout = Timeout(apiResponseTimeout seconds)

    val creator = system.actorOf(Props[RequestDispatcher], "RequestDispatcher")


    system.scheduler.schedule(0 millis, schedulerTimeout minute) {
      println("--------------------------------------------------")
      val weatherContainer = for (city <- cities) yield {
        val result = Await.result(creator ? city, timeout.duration)
        result.asInstanceOf[CityWeather]
      }
      val sortedWeather = weatherContainer.sortBy(_.main.temp)
      sortedWeather.foreach(println)
    }

  }
}
