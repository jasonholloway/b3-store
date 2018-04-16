package com.woodpigeon.b3

import cats.~>
import cats.free.Free

object Compilation {
  import Action._
  import LogAction._

  val compileActions: Action ~> Free[LogAction, ?] =
    λ[Action ~> Free[LogAction, ?]]({
      case ViewEntity(ref, behaviour) => 
        readLog(ref)
          .map(aggregateView(ref, behaviour))
      case UpdateEntity(ref, events) =>  //this will also trigger projections via behaviour
        appendLog(ref)
          .map(aggregateView(ref, ???))
    })

  def aggregateView[E <: Entity](ref: Ref[E], behaviour: Behaviour[E])(log: Log): E#View =
    log.foldLeft(behaviour.create(ref.name)) {
      (ac, ev) => behaviour.update(ac, ev).getOrElse(ac)
    }


  implicit def compileLogActions: LogAction ~> Transaction =
    λ[LogAction ~> Transaction]({
      case ReadLog(ref) => {
        ???
      }
      case AppendLog(ref) => {
        ???
      }
    })

}
