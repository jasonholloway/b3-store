package com.woodpigeon.b3

import scala.scalajs.js
import scala.scalajs.js.Promise
import scala.scalajs.js.typedarray.Int8Array

object JSTypes {
  type LogReader = js.Function1[Int8Array, Promise[Int8Array]]
  type LogWriter = js.Function1[Int8Array, Promise[Unit]]
}
