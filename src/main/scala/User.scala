package com.dh.chat
import colossus.protocols.websocket.WebsocketServerHandler
import colossus.protocols.websocket.subprotocols.rawstring.RawString

object User {
  type ClientRef = WebsocketServerHandler[RawString]
}
