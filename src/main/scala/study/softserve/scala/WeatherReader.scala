package study.softserve.scala

import akka.actor.{ActorRef, Props}
import akka.kafka.scaladsl.SendProducer
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import weather.CityWeather

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import scala.language.{implicitConversions, postfixOps}

object WeatherReader extends App {
  val producerSettings = ProducerInitializer.setUp()
  val producer = SendProducer(producerSettings)

  new WeatherReader(producer).run()
}

class WeatherReader(producer: SendProducer[String, Array[Byte]]) {
  def run(): Unit = {
    implicit val timeout: Timeout = Timeout(apiResponseTimeout seconds)
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    val actors: ActorRef = system.actorOf(RoundRobinPool(cities.size).props(Props[RequestActor]), "ActorPool")

    system.scheduler.schedule(0 millis, schedulerTimeout minute) {
      val futures = for (city <- cities) yield actors ? city

      futures.foreach(future => {
        future
          .mapTo[CityWeather]
          .onComplete(result => ProducerInitializer.send(producer, result.get))
      })
    }
  }
}
