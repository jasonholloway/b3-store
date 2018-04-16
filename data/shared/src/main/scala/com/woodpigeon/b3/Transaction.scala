package com.woodpigeon.b3

sealed trait Transaction[V]

object Transaction {
  case class Pure[V](v: V) extends Transaction[V]
  case class Load(logName: String, offset: Int) extends Transaction[EventSpan]
  case class Commit(logName: String, events: EventSpan) extends Transaction[Unit]
}
