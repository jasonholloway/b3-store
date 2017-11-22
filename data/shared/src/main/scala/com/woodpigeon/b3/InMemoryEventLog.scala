package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class InMemoryEventLog extends EventLog {

  private type Key = String
  private type Stream = (Int, List[Event])

  private var streams = Map[Key, Stream]()

  override def read(offsetMap: OffsetMap) : Future[Payload] = Future {
    val streamMap
          = offsetMap.offsets.flatMap {
              case (ref, readOffset) => streams.get(ref) map {
                  case (_, updates) => (ref, updates.reverseIterator.drop(readOffset))
              }
            }

    val eventLists = streamMap map { case (ref, events) => EventList(ref, events.toSeq) }

    Payload(eventLists.toSeq)
  }

  override def write(payload: Payload) : Future[Unit] = Future {
    payload.eventLists.foreach {
      case EventList(ref, events) => {
        append(ref, events)
      }
    }
  }


  def append(ref: String, update: Event): Unit = {
    streams = streams.get(ref) match {
      case Some((head, evs)) => streams + (ref -> (head + 1, update :: evs))
      case None => streams + (ref -> (1, update :: Nil))
    }
  }

  def append(ref: String, messages: Seq[Event]): Unit = {
    println(messages)
    messages.foreach(append(ref, _: Event))
  }

}
