package com.woodpigeon.b3

import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.Promise
import scala.util.control.NonFatal

@JSExportTopLevel("Fons")
@JSExportAll
class FonsJS(log: EventLog) {

  val inner = new Fons(log)

  def view(id: String) : Promise[Any] = {
    new Promise[Any]((resolve, reject) => {
      inner.view(id)
        .map(v => resolve(v))
        .recover({
          case NonFatal(e) => reject(e)
        })
    })
  }

}
