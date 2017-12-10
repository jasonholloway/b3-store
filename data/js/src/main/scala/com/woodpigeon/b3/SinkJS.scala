package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.StreamFragmentBatch
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.typedarray._
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise
import scala.scalajs.js

@JSExportTopLevel("Sink")
@JSExportAll
class SinkJS(log: EventLog) {

  val inner = new Sink(log)

  def commit(data: Int8Array): Promise[Unit] =
    inner.commit(StreamFragmentBatch.parseFrom(data.toArray)).toJSPromise

  def flush(): Promise[Unit] =
    inner.flush().toJSPromise

}