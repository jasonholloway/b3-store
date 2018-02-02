package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Sink(log: EventLog) {

  def commit(batch: StreamFragmentBatch): Future[Unit] = Future {
    log.write(batch)
  }

  def flush(): Future[Unit] = Future {
    //...
  }

}





//the Sink takes events in, executes them in batch
//or... nope!
//the Sink takes events and projects them immediately
//it stores them in a cumulative batch

//as soon as they're stored in the batch, they are available to the Fons - ie the Fons needs to read events
//from a two-layered log

//eventually we will flush the batch to the server


object Sink {
  implicit class Extensions(sink: Sink) {
    def commit(fragment: StreamFragment): Future[Unit] =
      sink.commit(StreamFragmentBatch(Seq(fragment)))

    def commit(ref: String, events: Seq[Event]): Future[Unit] =
      sink.commit(StreamFragment(ref, events))

  }
}
