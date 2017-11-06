package com.woodpigeon.b3

import java.io.{ByteArrayInputStream, InputStream}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class Fons(log: EventLog) {

  def view(id: String) : Future[InputStream] = Future {
    new ByteArrayInputStream(Array())
  }

}
