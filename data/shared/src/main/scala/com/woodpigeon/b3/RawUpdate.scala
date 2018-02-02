package com.woodpigeon.b3
import com.trueaccord.scalapb.Message
import com.woodpigeon.b3.schema.v100.{AddNote, Event, PutProduct, PutProductDetails}

import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}


case class Update(entityName: String, raw: RawUpdate)



trait RawUpdate {
  def asEvent(): Event
}


object RawUpdate {

  def unapply(update: RawUpdate): Option[Any]
    = Some(update.asEvent().inner.value)

  implicit def convert(ev: AddNote) : RawUpdate
    = () => Event().withAddNote(ev)

  implicit def convert(ev: PutProduct) : RawUpdate
    = () => Event().withPutProduct(ev)

  implicit def convert(ev: PutProductDetails) : RawUpdate
    = () => Event().withPutProductDetails(ev)



  implicit def updateToEvent(update: RawUpdate): Event
    = update.asEvent()

  implicit def updatesToEvents(updates: Seq[RawUpdate]) : Seq[Event]
    = updates map { _.asEvent() }

  implicit class RichEvent(ev: Event)
  {
    def asUpdate: Try[RawUpdate] = ev match {
      case Event(Event.Inner.AddNote(addNote)) => Success(addNote)
      case Event(Event.Inner.PutProduct(putProduct)) => Success(putProduct)
      case _ => Failure(new MatchError())
    }
  }

}


