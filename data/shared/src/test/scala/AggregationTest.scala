import com.woodpigeon.b3.{EventLog, Fons, InMemoryEventLog, Sink}
import com.woodpigeon.b3.schema.v100.{Event, _}
import com.woodpigeon.b3.Sink._
import org.scalatest.{Assertion, AsyncFreeSpec, FutureOutcome, fixture}
import com.woodpigeon.b3.Updates._
import Fons._
import org.scalatest.prop.Checkers

import scala.async.Async.{async, await}
import scala.concurrent.Future
import scala.language.implicitConversions


class AggregationTest extends AsyncFreeSpec {

  "EventList" - {

    "given a stream of events" - {

      val log = new InMemoryEventLog()
      val sink = new Sink(log)

      log.append("1234", Seq(
        AddNote("Hello").asEvent(0),
        AddNote("there").asEvent(1),
        AddNote("Jason!").asEvent(2)
      ))

      "should collect all those events" in {
        val fons = new Fons(log)

        fons.viewAs[EventList]("1234", "EventList")
          .map(aggr => {
            assert(aggr.events.length == 3)

            val phrase = aggr.events.flatMap {
              case Event(_, Event.Inner.AddNote(AddNote(message))) => Some(message)
              case _ => None
            }.mkString(" ")

            assert(phrase == "Hello there Jason!")
          })
      }

    }
  }

}
