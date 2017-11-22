package com.woodpigeon.b3

import com.trueaccord.scalapb.GeneratedMessage
import com.woodpigeon.b3.schema.v100.{AddNote, Event, PutProduct}

import scalapb.descriptors.ScalaType.Message

object Updates {

  implicit class Update(msg: GeneratedMessage) {

  }



  trait EventAdaptor[M] {
    def createEvent(message: M, version: Int) : Event
  }

  object EventAdaptor {
    def apply[M](create: (M, Int) => Event) : EventAdaptor[M] =
      (message: M, version: Int) => create(message, version)
  }


  implicit val addNoteAdaptor : EventAdaptor[AddNote] =
    EventAdaptor[AddNote]((m, v) => Event(version = v, inner = Event.Inner.AddNote(m)))

  implicit val putProductAdaptor : EventAdaptor[PutProduct] =
    EventAdaptor[PutProduct]((m, v) => Event(version = v, inner = Event.Inner.PutProduct(m)))


  implicit class UpdateEnricher[M](message: M)(implicit adaptor: EventAdaptor[M]) {
    def asEvent(version: Int) = adaptor.createEvent(message, version)
  }
}


