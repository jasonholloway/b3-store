package com.woodpigeon.b3

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

class LogCache {
  private val logMap = mutable.Map[String, Log]()

  def apply(logName: String): Log = synchronized {
    logMap.getOrElseUpdate(logName, new Log())
  }
}


case class LogSpan(start: Int, events: List[RawUpdate]) {
  val end: Int = start + events.length
}


class Log {
  private var updates: Option[LogSpan] = None

  def append(span: LogSpan): Try[Unit] = synchronized {
    updates match {
      case Some(currSpan) if span.start == currSpan.end =>
        updates = Some(LogSpan(currSpan.start, currSpan.events ++ span.events))
        Success()
      case None =>
        updates = Some(span)
        Success()
      case _ => Failure(new UnknownError())
    }
  }

  def read() : LogSpan = updates.getOrElse(LogSpan(0, List()))

  def clear(): Unit = updates = None
}
