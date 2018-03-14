package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.{AddNote, PutProductDetails}
import org.scalatest.AsyncFreeSpec
import scala.async.Async.{async,await}
import scala.concurrent.Future
import Behaviours._
import EventSpan._
import Ctx._
import scala.util.Try
import cats.implicits._

class ContextTests extends AsyncFreeSpec {

  def loadDummy(name: String): Ctx[Loaded[Dummy]] = Ref[Dummy](name).load

  implicit def ctxRunner = new CtxRunner[Future] {
    def run[V](ctx: Ctx[V], store: Any): Future[V] = ctx match {
      case CtxErr(e) => Future.failed(e)
      case CtxVal(v, s) => {
        //so, we'd commit to staging here...
        //but loading itself has to be done, and all functions executed
        //so, the combining, the flat-mapping of previous stages, all has to be done in order
        //as we flat map, we should then be storing up continuations - these are the real things to be accumulated
        //not SortedMaps!!! 
        Future(v)
      }
    }
  }


  "On viewing" - {

    val store = new FakeStore()
    store("dummy:A").append(
      EventSpan(AddNote("Hello"), AddNote("Jason"))
    )

    "events read from source" in {
      loadDummy("A")
        .map(e => assert(e.view.updates == Vector(AddNote("Hello"), AddNote("Jason")))) 
        .runAs[Future](store)
    }

  }


  "On writing" - {
    val store = new FakeStore()
    val x = new Context(store, store)

    val ref = Ref.product("MITTEN1")
    x.write(ref, PutProductDetails("Lovely mittens", 3.98f))

    println(store("Product#MITTEN1").read())
   
    "update is immediately viewable" in {
      x.view(ref)
        .map { v => assert(v.name == "Lovely mittens") }
    }

    "projections are immediately available" in {
      x.view(Ref.allProducts)
        .map { v => assert(SKU(v.skus.head) == ref.name) }
    }

    "before committing" - {
      "event isn't given to underlying store" in {
        store("Product#MITTEN1")
          .read() match {
            case Empty() => succeed
            case _ => fail("unexpected events in store before save!")
          }
      }
    }

    "after saving" - async {
      await { x.save() }

      "event appears in store" in {
        store("Product#MITTEN1")
          .read() match {
            case Full(start, events) =>
              assert(start == 0)
              assert(events.length == 1)
          }
      }
    }

  }
}

import scala.concurrent.ExecutionContext.Implicits.global

class FakeStore extends LogCache with LogSource with LogSink {
  def read(logName: String, offset: Int): Future[EventSpan] =
    Future(this(logName).read())

  def append(logName: String, updates: EventSpan): Try[Int] = 
    this(logName).append(updates)
      .map { _ => updates match {
        case full@EventSpan.Full(_, _) => full.end
        case EventSpan.Empty() => 0
      }
    }

  def commit(): Future[Unit] = ???
}


sealed trait Dummy extends Entity { type Key = String; type View = DummyView }

case class DummyView(updates: Vector[Any] = Vector())

object Dummy {
  implicit val dummyBehaviour : Behaviour[Dummy] = new Behaviour[Dummy] {
    def name(key: String): String = s"dummy:$key"
    def create(key: String): DummyView = DummyView()
    def update(ac: DummyView, update: Any): Option[DummyView] = Some(ac.copy(updates = ac.updates :+ update))
    def project(sink: Updater, key: String, before: DummyView, after: DummyView, update: Any): Future[_] = Future(())
  }
}

