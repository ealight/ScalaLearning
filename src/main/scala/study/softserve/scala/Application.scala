package study.softserve.scala

import akka.actor.{ActorRef, Props}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.SendProducer
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import com.myprotos.myproto.CityWeather
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.{ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.{implicitConversions, postfixOps}

object Application {

  def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    val config = ConfigFactory.load()
    val producerConfig = config.getConfig("akka.kafka.producer")
    val producerSettings: ProducerSettings[String, Array[Byte]] =
      ProducerSettings(producerConfig, new StringSerializer, new ByteArraySerializer)
    val producer = SendProducer(producerSettings)

    implicit val timeout: Timeout = Timeout(apiResponseTimeout seconds)

    val actors: ActorRef = system.actorOf(RoundRobinPool(cities.size).props(Props[RequestActor]), "ActorPool")

    system.scheduler.schedule(0 millis, schedulerTimeout minute) {
      val futures = for (city <- cities) yield actors ? city

      futures.foreach(future => {
        future
          .mapTo[CityWeather]
          .onComplete(result => {
            val send: Future[RecordMetadata] = producer
              .send(new ProducerRecord("test", result.get.toByteArray))

            send.onComplete(println)
          })
      })
    }

  }
}
