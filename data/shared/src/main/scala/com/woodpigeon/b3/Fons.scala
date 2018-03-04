package com.woodpigeon.b3

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Fons(loadAllEvents: String => Future[EventSpan]) {

  def view[E <: Entity](ref: Ref[E])(implicit handler: Behaviour[E]) : Future[E#View] =
    loadAllEvents(handler.name(ref.name))
      .map(aggregate(ref.name, _))

  private def aggregate[E <: Entity](name: E#Key, span : EventSpan)(implicit handler: Behaviour[E]) : E#View = span match {
    case EventSpan.Empty() => handler.create(name)
    case EventSpan.Full(_, events) =>  
      events.foldLeft(handler.create(name)) {
        (ac, ev) => handler.update(ac, ev.inner.value)
                    .getOrElse(ac)
      }
  }

}
