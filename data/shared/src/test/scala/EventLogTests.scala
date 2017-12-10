import com.woodpigeon.b3.{EventLog, InMemoryEventLog, Update}
import com.woodpigeon.b3.Update._
import com.woodpigeon.b3.schema.v100._
import org.scalatest.AsyncFreeSpec
import cats.implicits._

import scala.async.Async.{async, await}
import scala.language.implicitConversions
import scala.util.Success


class EventLogTests extends AsyncFreeSpec {

  def test[L <: EventLog](name: String, newLog: () => L) : Unit = {
    s"$name" - {

      "should allow writes" in async {
        val log = newLog()

        val fragments = Seq(
          StreamFragment("TEST:123", Seq[Update](
            AddNote("Hello"), AddNote("there"), AddNote("Jason!")
          ))
        )

        await { log.write(StreamFragmentBatch(fragments)) }

        assert(true)
      }

      "roundtrips happily" in async {
        val log = newLog()

        val batch = StreamFragmentBatch(Seq(
          StreamFragment("TEST:123", Seq[Update](
            AddNote("Hello"), AddNote("there"), AddNote("Jason!")
          ))
        ))

        await { log.write(batch) }

        val offsetMap = StreamOffsetMap(Map("TEST:123" -> 0))

        val returned = await { log.read(offsetMap) }

        val phrase = returned.fragments.head.events
            .map { _.inner }
            .flatMap {
              case Event.Inner.AddNote(AddNote(message)) => Some(message)
              case _ => None
            }.mkString(" ")

        assert(phrase == "Hello there Jason!")

        assert(returned.fragments.head.ref == "TEST:123")
      }


      "successive commits are concatenated" in async {
        val log = newLog()

        val batch1 = StreamFragmentBatch(Seq(
          StreamFragment("TEST:123", Seq[Update](
            AddNote("Hello")
          ))
        ))

        await { log.write(batch1) }

        val batch2 = StreamFragmentBatch(Seq(
          StreamFragment("TEST:123", Seq[Update](
            AddNote("there"), AddNote("Jason!")
          ))
        ))

        await { log.write(batch2) }

        val offsetMap = StreamOffsetMap(Map("TEST:123" -> 0))

        val returned = await { log.read(offsetMap) }

        val phrase = returned.fragments.flattenUpdates
                      .flatMap {
                        case Update(AddNote(message))
                          => Some(message)
                        case _ => None
                      }.mkString(" ")

        assert(phrase == "Hello there Jason!")
      }

    }
  }

  test("InMemoryEventLog", () => new InMemoryEventLog())


  implicit class EnrichedFragments(fragments: Seq[StreamFragment]) {
    def flattenUpdates: Seq[Update]
      = fragments.flatMap(_.events).toList.traverse(_.asUpdate).get

  }


}
