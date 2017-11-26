import com.woodpigeon.b3.{EventLog, InMemoryEventLog}
import com.woodpigeon.b3.Update._
import com.woodpigeon.b3.schema.v100.{Event, _}
import org.scalatest.{AsyncFreeSpec}

import scala.async.Async.{async, await}
import scala.language.implicitConversions


class EventLogTests extends AsyncFreeSpec {

  def test[L <: EventLog](name: String, newLog: () => L) : Unit = {
    s"$name" - {

      "should allow writes" in async {
        val log = newLog()

        val eventLists = Seq(
          EventList("TEST:123", Seq(
            AddNote("Hello").asEvent(0),
            AddNote("there").asEvent(1),
            AddNote("Jason!").asEvent(2)
          ))
        )

        await { log.write(Payload(eventLists)) }

        assert(true)
      }

      "roundtrips happily" in async {
        val log = newLog()

        val payload = Payload(Seq(
          EventList("TEST:123", Seq(
            AddNote("Hello").asEvent(0),
            AddNote("there").asEvent(1),
            AddNote("Jason!").asEvent(2),
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


      "successive commits are concatenated" in async {
        val log = newLog()

        val payload1 = Payload(Seq(
          EventList("TEST:123", Seq(
            AddNote("Hello").asEvent(0),
          ))
        ))

        await { log.write(payload1) }

        val payload2 = Payload(Seq(
          EventList("TEST:123", Seq(
            AddNote("there").asEvent(1),
            AddNote("Jason!").asEvent(2)
          ))
        ))

        await { log.write(payload2) }

        val offsetMap = OffsetMap(Map("TEST:123" -> 0))

        val returned = await { log.read(offsetMap) }

        val phrase = returned.eventLists.head.events.flatMap {
          case Event(_, Event.Inner.AddNote(AddNote(message))) => Some(message)
          case _ => None
        }.mkString(" ")

        assert(phrase == "Hello there Jason!")
      }

    }
  }

  test("InMemoryEventLog", () => new InMemoryEventLog())
}
