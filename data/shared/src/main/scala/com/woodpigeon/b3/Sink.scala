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


object Sink {
  implicit class Extensions(sink: Sink) {
    def commit(fragment: StreamFragment): Future[Unit] =
      sink.commit(StreamFragmentBatch(Seq(fragment)))

    def commit(ref: String, events: Seq[Event]): Future[Unit] =
      sink.commit(StreamFragment(ref, events))

  }
}
