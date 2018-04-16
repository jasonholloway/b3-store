package com.woodpigeon.b3

import cats.free.Free


sealed trait LogAction[V]

object LogAction {
  case class Pure[V](v: V) extends LogAction[V]
  case class ReadLog(ref: Ref[_ <: Entity]) extends LogAction[Log]
  case class AppendLog(ref: Ref[_ <: Entity]) extends LogAction[Log]

  def readLog(ref: Ref[_ <: Entity]): Free[LogAction, Log] =
    Free.liftF(ReadLog(ref))

  def appendLog(ref: Ref[_ <: Entity]): Free[LogAction, Log] =
    Free.liftF(AppendLog(ref))
}
