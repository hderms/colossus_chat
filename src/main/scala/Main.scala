package com.dh.chat

import colossus.IOSystem

import akka.actor._
import java.net.InetSocketAddress

object Main extends App {
  implicit val actorSystem = ActorSystem("COLOSSUS")

  implicit val ioSystem = IOSystem()
  val redisAddress = new InetSocketAddress("127.0.0.1", 6379)

  val chatServer = WebsocketExample.start(9005, redisAddress)
}
