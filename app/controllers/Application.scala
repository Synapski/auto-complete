package controllers

import play.api.mvc._
import play.api.libs.json.JsValue
import play.libs.Akka
import models.{WebSocketResponse, NewWebSocket, InputActor}
import akka.actor._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def socket = WebSocket.async[JsValue] { request =>

    val ws = Akka.system.actorOf(Props[InputActor])

    implicit val timeout = Timeout(1000)
    (ws ? NewWebSocket) map {
      case WebSocketResponse(in, out) =>
        (in, out)
    }
  }

}