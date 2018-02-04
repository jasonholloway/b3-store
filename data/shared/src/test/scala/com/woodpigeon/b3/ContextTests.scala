package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.{AddNote, PutProductDetails}
import org.scalatest.AsyncFreeSpec

import scala.concurrent.Future
import Behaviours._

import scala.util.Try

class ContextTests extends AsyncFreeSpec {


  case class DummyView(updates: List[Any] = List())

  sealed trait Dummy extends Entity { type Key = String; type View = DummyView }

  object Dummy {
    implicit val dummyBehaviour = new Behaviour[Dummy] {
      def name(key: String): String = s"dummy:$key"
      def create(key: String): DummyView = DummyView()
      def update(ac: DummyView, update: Any): Option[DummyView] = Some(ac.copy(updates = update :: ac.updates))
      def project(sink: Updater, key: String, before: DummyView, after: DummyView, update: Any): Future[_] = Future()
    }
  }





  "On viewing" - {
    val store = new FakeStore()
    store("dummy:A").append(
      LogSpan(0, List[RawUpdate](AddNote("Hello"), AddNote("Jason")))
    )

    val x = new Context(store, store)

    "events read from source" in {
      x.view(Ref[Dummy]("A"))
        .map(v =>
          assert(v.updates == List(AddNote("Hello"), AddNote("Jason")))
        )
    }

  }


  "On writing" - {
    val store = new FakeStore()
    val x = new Context(store, store)

    val ref = Ref.product("MITTEN1")
    x.write(ref, PutProductDetails("Lovely mittens", 3.98f))

    "update is immediately available" in {
      x.view(ref)
        .map(v => assert(v.name == "Lovely mittens"))
    }

    "projections are immediately available" in {
      x.view(Ref.allProducts)
        .map(v => assert(SKU(v.skus.head) == ref.name))
    }

  }


  class FakeStore extends LogCache with LogSource with LogSink {
    def read(logName: String, offset: Int): Future[LogSpan] =
      Future(this(logName).read())

    def append(logName: String, updates: LogSpan): Try[Int] = ???

    def commit(): Future[Unit] = ???
  }

}
