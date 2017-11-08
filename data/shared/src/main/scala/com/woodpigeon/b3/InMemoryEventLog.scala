package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.{Event, EventList, OffsetMap, Payload}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class InMemoryEventLog extends EventLog {

  private var streams = Map[String, Seq[Event]]()

  override def read(offsetMap: OffsetMap) : Future[Payload] = Future {
    val lists = offsetMap.offsets
                .map { case (ref, _) => EventList(ref, streams(ref)) }

    Payload(eventLists = lists.toSeq)
  }

  override def write(payload: Payload) : Future[Unit] = Future {
    payload.eventLists.foreach {
      case EventList(ref, events) => {
        streams = streams + (ref -> events)
      }
    }
  }
}
