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
import scala.collection.concurrent.TrieMap

import colossus.protocols.redis.{ RedisClient, RedisCommandParser }

object WebsocketExample {
  import Commands._
  def newHandler(ctx: ServerContext, redis: RedisClient[Callback], nicks: Nicknames.Directory): WebsocketServerHandler[RawString] = {
    new WebsocketServerHandler[RawString](ctx) with ProxyActor {
      val me = { this }

      override def preStart(): Unit = {
        sendMessage("Welcome to Dermochat")
      }

      override def shutdown(): Unit = {
        sendMessage("goodbye!")
        super.shutdown()
      }

      def handle = {
        case Nick(nick) => Nicknames.register(nicks, nick, me)
        case Send(nick, message) => Nicknames.fetch(nicks, nick) match {
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

  def start(port: Int, redisAddress: InetSocketAddress)(implicit io: IOSystem): Unit = {

    val nicks = new Nicknames.Directory(new TrieMap[Nicknames.Nick, WebsocketServerHandler[RawString]])
    WebsocketServer.start("websocket", port) { worker =>
      new WebsocketInitializer(worker) {
        implicit val env = worker

        val redis = Redis.client(redisAddress.getHostName, redisAddress.getPort, 1.second)

        def onConnect = ctx => {
          val handler = newHandler(ctx, redis, nicks)
          handler

        }

      }

    }
  }
}
