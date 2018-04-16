package com.woodpigeon.b3

import cats.free._

sealed trait Action[+V]

object Action {
  case class ViewEntity[E <: Entity](ref: Ref[E], behaviour: Behaviour[E]) extends Action[E#View]
  case class UpdateEntity[E <: Entity](ref: Ref[E], update: RawUpdate) extends Action[E#View]
  
  def viewEntity[E <: Entity](ref: Ref[E])(implicit behaviour: Behaviour[E]): Free[Action, E#View] = Free.liftF[Action, E#View](ViewEntity(ref, behaviour))
  def updateEntity[E <: Entity](ref: Ref[E], update: RawUpdate): Free[Action, E#View] = Free.liftF[Action, E#View](UpdateEntity(ref, update))
}
