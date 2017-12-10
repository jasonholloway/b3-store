package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100._

import scala.concurrent.Future

trait EventLog {
  def read(map: StreamOffsetMap) : Future[StreamFragmentBatch]
  def write(payload: StreamFragmentBatch) : Future[Unit]
}
