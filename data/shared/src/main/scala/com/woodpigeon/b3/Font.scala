package com.woodpigeon.b3

import scala.concurrent.Future

trait LogReader {
  def read(id: String) : Seq[Any]
}


class Font(log: LogReader) {
  def view(id: String) : Future[Any] = null
}
