package models

import play.api.libs.json.{Json, JsValue}
import play.api.libs.iteratee.{Concurrent, Iteratee, Enumerator}
import play.api.Logger
import akka.actor.Actor
import scala.concurrent.ExecutionContext.Implicits.global

case object NewWebSocket
case class WebSocketResponse(in: Iteratee[JsValue,_], out: Enumerator[JsValue])

class InputActor extends Actor {

  val (out, channel) = Concurrent.broadcast[JsValue]

  val in = Iteratee.foreach[JsValue] { json =>
    val content = (json \ "content").as[String]
    val recommendations = Search.recommendation(content, 10)
    channel.push(Json.toJson(recommendations))
  }.map { _ =>
  }

  def receive = {
    // NewWebSocket is a request, and we respond with our Enumerator/Iteratee pair
    case NewWebSocket => {
      sender ! WebSocketResponse(in, out)
    }
    // Standard application messages just get pushed into the Enumerator and up the websocket
    case msg:JsValue => {
      println(msg)
    }
    case _ => {
      Logger("WebSocketActor").info("Got a message we don't understand")
    }
  }

}
