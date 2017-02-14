package com.dh.chat

import colossus._
import colossus.service.Callback
import colossus.core._
import colossus.protocols.websocket._
import subprotocols.rawstring._

import scala.concurrent.duration._
import akka.actor.{ Actor, ActorRef, Props }
import akka.util.ByteString
import colossus.IOSystem
import colossus.service.{ Codec, DecodedResult }
import colossus.protocols.redis.Redis.defaults._

import colossus.protocols.redis._
import java.net.InetSocketAddress
import scala.concurrent.duration._

import colossus.protocols.redis.{ RedisClient, RedisCommandParser }

object Chat {
  import Commands._
  def newHandler(ctx: ServerContext, redis: RedisClient[Callback], nickNames: Directory): User.ClientRef = {
    new User.ClientRef(ctx) with ProxyActor {
      val me = { this }

      override def preStart(): Unit = {
        sendMessage("Welcome to Dermochat")
      }

      override def shutdown(): Unit = {
        sendMessage("goodbye!")
        super.shutdown()
      }

      def handle = {
        case Nick(nick) => Nicknames.register(nickNames, nick, me)
        case Send(nick, message) => Nicknames.fetch(nickNames, nick) match {
          case None => sendMessage("Couldn't find user by that nickname.")
          case Some(handle) => handle.sendMessage(message)
        }
        case "EXIT" => {
          disconnect()
        }
        case other => {
          sendMessage(s"unknown command: $other")
        }
      }

      def handleError(reason: Throwable): Unit = {}
      def receive = { case _ => () }
    }
  }

  def start(port: Int, redisAddress: InetSocketAddress, nameDirectory: Directory)(implicit io: IOSystem): Unit = {

    WebsocketServer.start("websocket", port) { worker =>
      new WebsocketInitializer(worker) {
        implicit val env = worker

        val redis = Redis.client(redisAddress.getHostName, redisAddress.getPort, 1.second)

        def onConnect = ctx => {
          val handler = newHandler(ctx, redis, nameDirectory)
          handler
        }
      }
    }
  }
}
