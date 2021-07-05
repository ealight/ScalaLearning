package study.softserve.scala

import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.SendProducer
import org.apache.kafka.clients.producer.{ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import weather.WeatherReply

import scala.concurrent.{ExecutionContextExecutor, Future}

object ProducerInitializer {
  def setUp(): ProducerSettings[String, Array[Byte]] = {
    val producerConfig = config.getConfig("akka.kafka.producer")
    ProducerSettings(producerConfig, new StringSerializer, new ByteArraySerializer)
  }

  def send(producer: SendProducer[String, Array[Byte]], result: WeatherReply): Unit = {
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    val kafkaConfig = config.getConfig("application.akka.kafka")
    val topic = kafkaConfig.getString("produce-topic")

    val send: Future[RecordMetadata] =
      producer.send(new ProducerRecord(topic, result.toByteArray))

    send.onComplete(println)
  }
}
