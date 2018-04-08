package com.woodpigeon.b3

import cats.free._

sealed trait Action[+V]

object Action {
  case class Read[E <: Entity](ref: Ref[E]) extends Action[E#View]
  case class Update[E <: Entity](ref: Ref[E], update: RawUpdate) extends Action[E#View]
  
  def read[E <: Entity](ref: Ref[E]): Free[Action, E#View] = Free.liftF[Action, E#View](Read(ref))
  def update[E <: Entity](ref: Ref[E], update: RawUpdate): Free[Action, E#View] = Free.liftF[Action, E#View](Update(ref, update))
}
