package com.woodpigeon.b3

import cats.Semigroup

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

  def ++(other: LogSpan): Try[LogSpan] =
    if(other.start == end) Success(LogSpan(start, events ++ other.events))
    else Failure(new Error("Can't join mismatched LogSpans"))

  implicit class RichLogSpanOption(span: Option[LogSpan]) {
    def +++(other: Option[LogSpan]) = ???
  }


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

  def read() : Option[LogSpan] = updates

  def clear(): Unit = updates = None
}
