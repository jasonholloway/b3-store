package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class InMemoryEventLog extends EventLog {

  private type Key = String
  private type Stream = (Int, List[Event])


  private var streams = Map[Key, Stream]()

  override def read(offsetMap: OffsetMap) : Future[Payload] = {
    println("GAHHH")

    val streamMap = offsetMap.offsets
                    .flatMap { case (ref, readOffset) =>
                      streams.get(ref) match {
                        case Some((_, updates)) =>
                          println(updates)
                          Some(EventList(ref, updates.drop(readOffset)))
                        case None => None
                      }
                    }
    println("hello")
    Future(Payload(eventLists = streamMap.toSeq))
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
