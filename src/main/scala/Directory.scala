package com.dh.chat
import Nicknames.NickName
import scala.collection.concurrent.TrieMap

// Interface describing a user directory 
trait INameDirectory {
  def get(nick: NickName): Option[User.ClientRef]
  def register(name: String, client: User.ClientRef): Either[String, NickName]
}

//Concrete type of directory that uses a concurrent TrieMap to store nick -> ClientRef
class Directory(val nicks: TrieMap[NickName, User.ClientRef]) extends INameDirectory {
  def get(nick: NickName): Option[User.ClientRef] = {
    nicks.get(nick)
  }

  def register(name: String, client: User.ClientRef): Either[String, NickName] = {
    val nick = new NickName(name)

    Nicknames
      .check(nick)
      .right
      .map { nick =>
        {
          nicks += (new NickName(nick.name) -> client)
          nick
        }
      }
  }
}
object Directory {
  def default = {
    new Directory(new TrieMap[Nicknames.NickName, User.ClientRef])
  }
}
