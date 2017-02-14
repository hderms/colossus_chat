package com.dh.chat

import colossus.IOSystem

import akka.actor._
import java.net.InetSocketAddress

object Main extends App {
  implicit val actorSystem = ActorSystem("DermoChat")

  implicit val ioSystem = IOSystem()
  val nameDirectory = Directory.default
  val redisAddress = new InetSocketAddress("127.0.0.1", 6379)
  val chatServer = Chat.start(9005, redisAddress, nameDirectory)
}
