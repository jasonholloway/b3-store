import com.woodpigeon.b3.{EventLog, InMemoryEventLog}
import com.woodpigeon.b3.schema.v100.{Event, _}
import org.scalatest.AsyncFreeSpec

import scala.async.Async.{async, await}
import scala.language.implicitConversions


class EventLogTests extends AsyncFreeSpec {

  def test[L <: EventLog](name: String, newLog: () => L) : Unit = {
    s"$name" - {

      "should allow writes" in async {
        val log = newLog()

        val eventLists = Seq(
          EventList("TEST:123", Seq(
            Event(0).withAddNote(AddNote("Hello")),
            Event(1).withAddNote(AddNote("there")),
            Event(2).withAddNote(AddNote("Jason!")),
          ))
        )

        await { log.write(Payload(eventLists)) }

        assert(true)
      }

      "roundtrips happily" in async {
        val log = newLog()

        val payload = Payload(Seq(
          EventList("TEST:123", Seq(
            Event(0).withAddNote(AddNote("Hello")),
            Event(1).withAddNote(AddNote("there")),
            Event(2).withAddNote(AddNote("Jason!")),
          ))
        ))

        await { log.write(payload) }

        val offsetMap = OffsetMap(Map("TEST:123" -> 0))

        val returned = await { log.read(offsetMap) }

        val phrase = returned.eventLists.head.events.flatMap {
          case Event(_, Event.Inner.AddNote(AddNote(message))) => Some(message)
          case _ => None
        }.mkString(" ")

        assert(phrase == "Hello there Jason!")

        assert(returned.eventLists.head.ref == "TEST:123")
      }

    }
  }

  test("InMemoryEventLog", () => new InMemoryEventLog())
}
