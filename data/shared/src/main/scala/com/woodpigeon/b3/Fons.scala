package com.woodpigeon.b3

import com.trueaccord.scalapb.GeneratedMessage
import com.woodpigeon.b3.schema.v100._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.async.Async.{async, await}

class Fons(log: EventLog) {

  def view(streamRef: String, aggrType: String) : Future[GeneratedMessage] = async {
    val offsetMap = StreamOffsetMap(offsets = Map(streamRef -> 0))
    val batch = await { log.read(offsetMap) }
    val events = batch.fragments.head.events
    StreamFragment(events = events)
  }

}


object Fons {
  implicit class Extensions(fons: Fons) {
    def viewAs[V](streamRef: String, aggrType: String): Future[V] =
      fons.view(streamRef, aggrType).map(_.asInstanceOf[V])

  }
}


