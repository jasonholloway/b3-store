//package com.woodpigeon.b3
//
//import com.woodpigeon.b3.schema.v100.PutProductDetails
//import org.scalatest.AsyncFreeSpec
//
//import scala.concurrent.Future
//
//class StagingTests extends AsyncFreeSpec {
//
//  val dummySource = new LogSource {
//    def read(logName: String, offset: Int): Future[Seq[RawUpdate]] = ???
//  }
//
//  val dummySink = new LogSink {
//    def commit(): Future[Unit] = ???
//    def append(logName: String, updates: Seq[RawUpdate]): Int = ???
//  }
//
//  val staging = new Staging(dummySource, dummySink)
//
//  "Written updates can be read" - {
//    staging.append("LOG", Seq(PutProductDetails("frog", 0.50f) ))
//    staging.append("LOG", Seq(PutProductDetails("frog2", 0.50f) ))
//
//    staging.read("LOG", 0)
//        .map(results => {
//          assert(results.head.inner.putProductDetails.get.name == "frog")
//          assert(results.tail.head.inner.putProductDetails.get.name == "frog2")
//        })
//  }
//
//}
