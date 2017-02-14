package com.dh.chat

object Nicknames {
  class NickName(val name: String) extends AnyVal

  def check(nick: NickName): Either[String, NickName] = nick.name.length match {
    case i: Int if (i < 5) => Left("Nick too small")
    case i: Int => Right(nick)
  }

  def fetch[A](directory: A with INameDirectory, name: String): Option[User.ClientRef] = {
    val nick = new NickName(name)
    directory.get(nick)
  }

  def register[A](directory: A with INameDirectory, name: String, client: User.ClientRef): Either[String, NickName] = {
    directory.register(name, client)
  }
}
