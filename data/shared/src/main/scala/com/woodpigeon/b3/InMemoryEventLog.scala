package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100._
import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import Update._
import cats.implicits._
import scala.util.{Failure, Success}

class InMemoryEventLog extends EventLog {

  private type Key = String
  private type Stream = (Int, List[Update])

  private var store = mutable.Map[Key, Stream]()

  override def read(offsetMap: StreamOffsetMap) : Future[StreamFragmentBatch] = Future {
    val streams = offsetMap.offsets.flatMap {
                    case (ref, readOffset) => store.get(ref) map {
                        case (_, updates) => (ref, updates.reverseIterator.drop(readOffset))
                    }
                  }

    val fragments = streams map {
      case (ref, updates) => StreamFragment(ref, updates.toSeq)
    }

    StreamFragmentBatch(fragments.toSeq)
  }

  override def write(batch: StreamFragmentBatch) : Future[Unit] = Future {
    batch.fragments.foreach {
      case StreamFragment(ref, events) =>
        events.toList.traverse(_.asUpdate) match {
          case Success(updates) => stream(ref).append(updates: _*)
          case Failure(error) => throw error
        }
    }
  }


  //noinspection ConvertExpressionToSAM
  def stream(ref: Key) : AppendableStream =
    new AppendableStream {
      override def append(newUpdates: Update*): Unit = store.get(ref) match {
        case Some((head, updates)) =>
          store(ref) = (head + newUpdates.length, newUpdates.foldLeft(updates){(ac, u) => u :: ac})
        case None =>
          store(ref) = (newUpdates.length, newUpdates.reverse.toList)
      }
    }


  trait AppendableStream {
    def append(updates: Update*) : Unit
  }

}
