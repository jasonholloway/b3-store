package com.woodpigeon.b3

import java.io.ByteArrayInputStream

import com.woodpigeon.b3.schema.v100.Payload

import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}
import scala.scalajs.js.typedarray._
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise
import scala.concurrent.ExecutionContext.Implicits.global
import scala.async.Async.{async, await}

@JSExportTopLevel("Sink")
@JSExportAll
class SinkJS(log: EventLog) {

  val inner = new Sink(new InMemoryEventLog())

  def commit(data: Int8Array): Promise[Unit] =
    inner.commit(Payload.parseFrom(data.toArray)).toJSPromise

  def flush(): Promise[Unit] =
    inner.flush().toJSPromise

}