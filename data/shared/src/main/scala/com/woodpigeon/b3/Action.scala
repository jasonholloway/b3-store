package com.woodpigeon.b3

import cats.free._
import com.woodpigeon.b3.{ Entity, Loaded }

sealed trait Action[+V]

object Action {
  case class Read[E <: Entity](ref: Ref[E]) extends Action[E]
  case class Update[E <: Entity](ref: Ref[E], update: RawUpdate) extends Action[E]
  
  def read[E <: Entity](ref: Ref[E]): Free[Action, Loaded[E]] = Free.liftF[Action, E](Read(ref))
  def update[E <: Entity](ref: Ref[E], update: RawUpdate): Free[Action, E] = Free.liftF(Update(ref, update))
}
