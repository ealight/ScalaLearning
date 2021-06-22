package study.softserve.scala

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class RequestDispatcher extends Actor {

  val requesters: ActorRef = context.actorOf(Props[RequestActor].withRouter(RoundRobinPool(cities.size)), "RequestDispatcher")
  implicit val timeout: Timeout = Timeout(apiResponseTimeout seconds)

  override def receive: Receive = {
    case request => {
      sender() ! Await.result(requesters ? request, timeout.duration)
    }
  }
}
