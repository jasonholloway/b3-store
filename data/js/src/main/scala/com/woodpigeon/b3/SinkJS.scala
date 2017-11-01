package com.woodpigeon.b3

import java.io.ByteArrayInputStream

import faithful.Promise

import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.typedarray.Uint8Array

@JSExportTopLevel("Sink")
@JSExportAll
class SinkJS(log: EventLog) {
  val inner = new Sink(log)

  def commit(data: Uint8Array): Promise[Unit] = {
    val stream = new ByteArrayInputStream(data.map(_.toByte).toArray)
    val promise = new Promise[Unit]()

    inner.commit(stream)
      .map(_ => stream.close())
      .map(_ => promise.success())

    promise
  }

  def flush(): Promise[Unit] = {
    inner.flush()
    val promise = new Promise[Unit]()
    promise.success()
    promise
  }
}