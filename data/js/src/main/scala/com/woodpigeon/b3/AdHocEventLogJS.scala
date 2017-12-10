package com.woodpigeon.b3

import java.io.ByteArrayOutputStream

import com.woodpigeon.b3.JSTypes._
import com.woodpigeon.b3.schema.v100.{StreamOffsetMap, StreamFragmentBatch}
import scala.async.Async.{async, await}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}
import scala.scalajs.js.typedarray._


@JSExportTopLevel("AdHocEventLog")
@JSExportAll
class AdHocEventLogJS(reader: LogReader, writer: LogWriter) extends EventLog {

  override def read(offsetMap: StreamOffsetMap) : Future[StreamFragmentBatch] = async {
    val requestBuffer = new ByteArrayOutputStream(4096)
    offsetMap.writeTo(requestBuffer)

    val responseBuffer = await { reader(requestBuffer.toByteArray.toTypedArray).toFuture }

    StreamFragmentBatch.parseFrom(responseBuffer.toArray)
  }

  override def write(batch: StreamFragmentBatch) : Future[Unit] = async {
    val buffer = new ByteArrayOutputStream(4096)
    batch.writeTo(buffer)

    await { writer(buffer.toByteArray.toTypedArray).toFuture }
  }

}
