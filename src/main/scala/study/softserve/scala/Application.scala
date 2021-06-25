package study.softserve.scala

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import study.softserve.scala.JSONParser.CityWeather

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import scala.language.{implicitConversions, postfixOps}

object Application {

  def main(args: Array[String]): Unit = {
    implicit val timeout: Timeout = Timeout(apiResponseTimeout seconds)
    implicit val contx: ExecutionContextExecutor = system.dispatcher

    val actors: ActorRef = system.actorOf(RoundRobinPool(cities.size).props(Props[RequestActor]), "ActorPool")

    system.scheduler.schedule(0 millis, schedulerTimeout minute) {
      println("--------------------------------------------------")
      val futures = for (city <- cities) yield actors ? city

      system.scheduler.scheduleOnce(apiResponseTimeout seconds) {
        val futuresResult = futures
          .flatMap(future =>
            future
              .mapTo[CityWeather]
              .value
              .map(x => x.get))

        val sorted = futuresResult.sorted

        sorted.foreach(println)
      }
    }

  }
}
