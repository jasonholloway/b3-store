package com.woodpigeon.b3

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
    val message = await { inner.view(id) }
    message.toByteArray.toTypedArray
  }.toJSPromise

}
