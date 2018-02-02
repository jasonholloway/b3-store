package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.PutProductDetails
import org.scalatest.AsyncFreeSpec

import scala.concurrent.Future
import Behaviours._

import scala.util.Try

class ContextTests extends AsyncFreeSpec {

  val dummySource = new LogSource {
    def read(logName: String, offset: Int): Future[LogSpan] = ???
  }

  val dummySink = new LogSink {
    def commit(): Future[Unit] = ???
    def append(logName: String, updates: LogSpan): Try[Int] = ???
  }

  val x = new Context(dummySource, dummySink)


  "When an update is written" - {
    x.write(Ref.product("MITTEN1"), PutProductDetails("Lovely mittens", 3.98f))

    "update is immediately available" in {
      x.view(Ref.product("MITTEN1"))
        .map(v => assert(v.name == "Lovely mittens"))
    }

    "projections are immediately available" in {
      x.view(Ref.allProducts)
        .map(v => assert(v.skus.head == "MITTEN1"))
    }
  }

}
