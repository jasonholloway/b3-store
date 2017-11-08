package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.{Event, EventList, Payload}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Sink(log: EventLog) {

  def commit(payload: Payload): Future[Unit] = Future {
    log.write(payload)
  }

  def flush(): Future[Unit] = Future {
    //...
  }

}


object Sink {
  implicit class Extensions(sink: Sink) {
    def commit(eventList: EventList): Future[Unit] =
      sink.commit(Payload(Seq(eventList)))

    def commit(ref: String, events: Seq[Event]): Future[Unit] =
      sink.commit(EventList(ref, events))

  }
}
