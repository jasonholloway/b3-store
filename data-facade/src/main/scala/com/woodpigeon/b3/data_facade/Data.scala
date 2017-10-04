package com.woodpigeon.b3.data_facade

import java.io.ByteArrayInputStream
import java.nio.ByteBuffer

import io.scalajs.nodejs.console
import io.scalajs.npm.mpromise._

import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.typedarray.{ArrayBuffer, Int8Array, TypedArrayBuffer, TypedArrayBufferOps, Uint8Array}

@JSExportTopLevel("Data")
@JSExportAll
class Data() {
  val inner = new com.woodpigeon.b3.data.Data()

  def commit(data: ArrayBuffer): Promise[Unit] = {
    val typedArray = new Int8Array(data)
    val stream = new ByteArrayInputStream(typedArray.toArray)
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