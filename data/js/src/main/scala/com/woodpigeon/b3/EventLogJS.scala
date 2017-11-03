package com.woodpigeon.b3

import com.woodpigeon.b3.JSTypes._
import faithful.Promise

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

object JSTypes {
  type EventReader = js.Function1[Any, Promise[Any]]
  type EventWriter = js.Function1[Any, Promise[Boolean]]
}


@JSExportTopLevel("EventLog")
@JSExportAll
class EventLogJS(read: EventReader, write: EventWriter) {

  read(null)

  val result = write(null)

}
