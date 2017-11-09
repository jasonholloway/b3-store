package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.{Event, EventList, OffsetMap, Payload}

import scala.concurrent.Future

trait EventLog {
  def read(map: OffsetMap) : Future[Payload]
  def write(payload: Payload) : Future[Unit]
}
