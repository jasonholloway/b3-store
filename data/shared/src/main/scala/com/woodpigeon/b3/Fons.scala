package com.woodpigeon.b3

import com.trueaccord.scalapb.GeneratedMessage
import com.woodpigeon.b3.schema.v100.NoteList

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.async.Async.{async, await}

class Fons(log: EventLog) {

  def view(id: String) : Future[GeneratedMessage] = async {
    NoteList(notes = Seq("Hello", "Jason!"))
  }

}
