package com.woodpigeon.b3
import com.trueaccord.scalapb.Message
import com.woodpigeon.b3.schema.v100.{AddNote, Event, PutProduct}

import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}


trait Update {
  def asEvent(): Event
}


object Update {

  def unapply(update: Update): Option[Any]
    = Some(update.asEvent().inner.value)

  implicit def convert(ev: AddNote) : Update
    = () => Event().withAddNote(ev)

  implicit def convert(ev: PutProduct) : Update
    = () => Event().withPutProduct(ev)


  implicit def updateToEvent(update: Update): Event
    = update.asEvent()

  implicit def updatesToEvents(updates: Seq[Update]) : Seq[Event]
    = updates map { _.asEvent() }

  implicit class RichEvent(ev: Event)
  {
    def asUpdate: Try[Update] = ev match {
      case Event(Event.Inner.AddNote(addNote)) => Success(addNote)
      case Event(Event.Inner.PutProduct(putProduct)) => Success(putProduct)
      case _ => Failure(new MatchError())
    }
  }

}


