package com.woodpigeon.b3

sealed trait LogAction[V]

object LogAction {
  case class Read(ref: Ref[_ <: Entity]) extends LogAction[Log]
  case class Append(ref: Ref[_ <: Entity]) extends LogAction[Log]
}
