package com.woodpigeon.b3

import java.io.{ByteArrayOutputStream}
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.Promise
import scala.scalajs.js.typedarray._
import scala.scalajs.js.JSConverters._
import scala.async.Async.{async, await}

@JSExportTopLevel("Fons")
@JSExportAll
class FonsJS(log: EventLogJS) {

  val inner = new Fons(new EventLog {})

  def view(id: String) : Promise[Int8Array] = async {
    val str = await { inner.view(id) }
    new ByteArrayOutputStream(256).toByteArray.toTypedArray
  }.toJSPromise

}
