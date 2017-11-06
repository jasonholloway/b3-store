package com.woodpigeon.b3

import java.io.ByteArrayInputStream
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.typedarray.Int8Array
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise
import scala.concurrent.ExecutionContext.Implicits.global
import scala.async.Async.{async, await}

@JSExportTopLevel("Sink")
@JSExportAll
class SinkJS(log: EventLogJS) {

  val inner = new Sink(new EventLog {})

  def commit(data: Int8Array): Promise[Unit] = async {
    val stream = new ByteArrayInputStream(data.toArray)
    await { inner.commit(stream) }
    stream.close()
  }.toJSPromise

  def flush(): Promise[Unit] =
    inner.flush().toJSPromise

}