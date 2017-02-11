package com.dh.chat

import colossus.service.Callback
import colossus.protocols.websocket.WebsocketServerHandler
import colossus.protocols.websocket.subprotocols.rawstring._
import scala.collection.concurrent.TrieMap
object Nicknames {
  class Directory(val nicks: TrieMap[Nick, WebsocketServerHandler[RawString]])
  class Nick(val name: String) extends AnyVal

  def check(nick: Nick): Either[String, Nick] = nick.name.length match {
    case i: Int if (i < 5) => Left("Nick too small")
    case i: Int => Right(nick)
  }

  def fetch(directory: Directory, name: String): Option[WebsocketServerHandler[RawString]] = {
    val nick = new Nick(name)

    directory.nicks get nick

  }

  def register(directory: Directory, name: String, client: WebsocketServerHandler[RawString]): Either[String, Nick] = {
    val nick = new Nick(name)

    Nicknames.check(nick) match {
      case Left(err) => Left(err)
      case Right(nick) => {
        directory.nicks += (new Nick(nick.name) -> client)
        Right(nick)
      }
    }
  }
}
