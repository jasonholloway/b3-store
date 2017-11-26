package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.{AddNote, Event, PutProduct}
import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}


trait Update {
  def asEvent(v: Int): Event
}


object Update {

  implicit def convert(ev: AddNote) : Update
    = (v: Int) => Event(v).withAddNote(ev)

  implicit def convert(ev: PutProduct) : Update
    = (v: Int) => Event(v).withPutProduct(ev)


  implicit class RichEvent(ev: Event)
  {
    def asUpdate: Try[Update] = ev match {
      case Event(_, Event.Inner.AddNote(addNote)) => Success(addNote)
      case Event(_, Event.Inner.PutProduct(putProduct)) => Success(putProduct)
      case _ => Failure(new MatchError())
    }
  }

}


