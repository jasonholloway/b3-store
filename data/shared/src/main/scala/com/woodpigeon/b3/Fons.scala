package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Fons(logSource: LogSource) {

  def view[E <: Entity](ref: Ref[E])(implicit handler: Behaviour[E]) : Future[E#View] =
    loadEvents(handler.name(ref.name))
      .map { aggregate(ref.name, _) }


  private def aggregate[E <: Entity](name: E#Key, events : Seq[Event])(implicit handler: Behaviour[E]) : E#View =
    events.map { _.inner.value }
      .foldLeft(handler.create(name)) {
        (ac, u) => handler.update(ac, u)
                    .getOrElse(ac)
      }


  private def loadEvents(ref: String) : Future[Seq[Event]] =
    logSource.read(ref, 0)
      .map(_.events.map(_.asEvent()))

  //so the Fons should take the log, the Context, etc

}


case class LogUpdate[E <: Entity](ref: Ref[E], update: RawUpdate)

