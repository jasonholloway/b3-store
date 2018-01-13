package com.woodpigeon.b3

import com.trueaccord.scalapb.GeneratedMessage
import com.woodpigeon.b3.schema.v100._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.async.Async.{async, await}
import scala.reflect.ClassTag

case class BadAggregationTypeException() extends Exception

class Fons(log: EventLog) {

  def view[V :Creator :Updater](streamRef: String) : Future[V] =
    loadEvents(streamRef) map { aggregate[V] }


  private def aggregate[V :Creator :Updater](events : Seq[Event])(implicit creator: Creator[V], updater: Updater[V]) : V = {
    val view = creator.create()
    events.foldLeft(view) { updater.update }
  }


  private def loadEvents(ref: String) = {
    val offsetMap = StreamOffsetMap(offsets = Map(ref -> 0))
    log.read(offsetMap)
      .map(_.fragments.head.events)
  }

}



trait Updater[V] {
  def update(view : V, event: Event) : V
}

trait Creator[T] {
  def create() : T
}


object Creators {

  implicit val newStreamFragment : Creator[StreamFragment] =
    () => StreamFragment()

  implicit val newProductSet : Creator[ProductSet] =
    () => ProductSet()

}


object Updaters {

  implicit val updateStreamFragment : Updater[StreamFragment] =
    (fragment: StreamFragment, event: Event) => {
      fragment
    }

  implicit val updateProductSet : Updater[ProductSet] =
    (productSet: ProductSet, event: Event) => {
      productSet
    }

}


object Fons {

}


