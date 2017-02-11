package com.dh.chat

object Commands {
  val Nick = """nick ([a-zA-Z]{0,10})""".r
  val Send = """send ([a-zA-Z]{0,10}) ([a-zA-Z ]{0,10})""".r
}
