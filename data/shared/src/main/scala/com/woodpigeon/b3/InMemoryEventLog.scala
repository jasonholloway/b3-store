package com.woodpigeon.b3

import com.trueaccord.scalapb.GeneratedMessage
import com.woodpigeon.b3.schema.v100.{Event, EventList, OffsetMap, Payload}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class InMemoryEventLog extends EventLog {

  private type Key = String
  private type Update = Any
  private type Stream = (Int, List[Update])


  private var streams = Map[Key, Stream]()

  override def read(offsetMap: OffsetMap) : Future[Payload] = Future {
    val streamMap = offsetMap.offsets
                    .flatMap { case (ref, readOffset) =>
                      streams.get(ref) match {
                        case Some((_, updates)) =>
                          val events = updates.drop(readOffset).map(_ => Event())
                          Some(EventList(ref, events))
                        case None => None
                      }
                    }

    Payload(eventLists = streamMap.toSeq)
  }

  override def write(payload: Payload) : Future[Unit] = Future {
    payload.eventLists.foreach {
      case EventList(ref, events) => {
        append(ref, events.map(_.inner.value))
      }
    }
  }


  def append(ref: String, update: Update): Unit = {
    streams = streams.get(ref) match {
      case Some((head, evs)) => streams + (ref -> (head + 1, update :: evs))
      case None => streams + (ref -> (1, update :: Nil))
    }
  }

  def append(ref: String, messages: Seq[Update]): Unit =
    messages.foreach(append(ref, _: Update))

}
