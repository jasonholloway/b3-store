package com.woodpigeon.b3

import cats.data._
import cats.implicits._
import com.woodpigeon.b3.schema.v100._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Fons(logSource: LogSource) {

  def view[E <: Entity](ref: Ref[E])(implicit handler: Behaviour[E]) : OptionT[Future, E#View] =
    loadEvents(handler.name(ref.name))
      .map { aggregate(ref.name, _) }


  private def aggregate[E <: Entity](name: E#Key, events : Seq[Event])(implicit handler: Behaviour[E]) : E#View =
    events.map { _.inner.value }
      .foldLeft(handler.create(name)) {
        (ac, u) => handler.update(ac, u)
                    .getOrElse(ac)
      }


  private def loadEvents(ref: String) : OptionT[Future, List[Event]] =
    logSource.read(ref, 0)
        .map(_.events.map(_.asEvent()))


}


case class LogUpdate[E <: Entity](ref: Ref[E], update: RawUpdate)

