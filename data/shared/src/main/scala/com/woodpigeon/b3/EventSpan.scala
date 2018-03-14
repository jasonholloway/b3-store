package com.woodpigeon.b3

import cats.Monoid
import com.woodpigeon.b3.schema.v100.Event
import scala.util.{ Failure, Success, Try }

sealed trait EventSpan

object EventSpan {
  def apply(start: Int, updates: RawUpdate*): EventSpan = Full(start, updates.map(_.asEvent).toList)
  def apply(updates: RawUpdate*): EventSpan = apply(0, updates:_*)
  
  val empty: EventSpan = Empty()

  case class Empty() extends EventSpan
  case class Full(start: Int, events: List[Event]) extends EventSpan {
    def end: Int = start + events.length
  }


  sealed trait EventSpanError extends Throwable
  case object MismatchedEventSpans extends EventSpanError
  case object StrangeEventSpans extends EventSpanError


  implicit class RichEventSpan(self: EventSpan) {
    def append(other: EventSpan): EventSpanCombo = (self, other) match {
      case (a@Full(_, _), b@Full(_, _)) =>
        if(a.end == b.start) Success(Full(a.start, a.events ++ b.events))
        else Failure(MismatchedEventSpans)

      case (a@Full(_, _), Empty()) => Success(a)
      case (Empty(), b@Full(_, _)) => Success(b)
      case (Empty(), Empty()) => Success(Empty())
      case _ => Failure(StrangeEventSpans)
    }
  }


  type EventSpanCombo = Try[EventSpan]

  implicit val eventSpanCombo = new Monoid[EventSpanCombo] {
    def empty: EventSpanCombo = Success(Empty())
    def combine(x: EventSpanCombo, y: EventSpanCombo): EventSpanCombo = (x, y) match {
      case (Success(a), Success(b)) => a append b
      case (f@Failure(_), _) => f
      case (_, f@Failure(_)) => f
    }
  }

}
