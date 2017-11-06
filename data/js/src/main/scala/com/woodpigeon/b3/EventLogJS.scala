package com.woodpigeon.b3

import com.woodpigeon.b3.JSTypes._

import scala.scalajs.js.Promise
//import faithful.Promise

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

object JSTypes {
  type RefMap[P] = js.Dictionary[P]
  type EventReader = js.Function1[RefMap[Any], Promise[RefMap[Any]]]
  type EventWriter = js.Function1[RefMap[Any], Promise[Boolean]]
}


@JSExportTopLevel("EventLog")
@JSExportAll
class EventLogJS(read: EventReader, write: EventWriter) {

  read(null)

  val result = write(null)

}
