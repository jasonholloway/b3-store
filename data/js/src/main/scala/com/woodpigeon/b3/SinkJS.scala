package com.woodpigeon.b3

import java.io.ByteArrayInputStream
import java.nio.ByteBuffer

import io.scalajs.nodejs.console
import io.scalajs.npm.mpromise._

import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.typedarray.Uint8Array

@JSExportTopLevel("Sink")
@JSExportAll
class SinkJS() {
  val inner = new Sink()

  def commit(data: Uint8Array): Promise[Unit] = {
    val stream = new ByteArrayInputStream(data.map(_.toByte).toArray)
    val promise = new Promise[Unit]()

    inner.commit(stream)
      .map(_ => stream.close())
      .map(_ => promise.fulfill())

    promise
  }

  def flush(): Promise[Unit] = {
    inner.flush()
    val promise = new Promise[Unit]()
    promise.fulfill()
    promise
  }
}