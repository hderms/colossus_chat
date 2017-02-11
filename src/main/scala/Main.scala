package com.dh.chat

import colossus._
import akka.actor._
import java.net.InetSocketAddress



object Main extends App {
  implicit val actorSystem = ActorSystem("COLOSSUS")

  implicit val ioSystem = IOSystem()

  val chatServer = Chat.start(9005)
}
