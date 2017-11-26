import com.woodpigeon.b3._
import com.woodpigeon.b3.schema.v100.{AddNote, Event, Update => _, _}
import org.scalatest.{Assertion, AsyncFreeSpec, FutureOutcome, fixture}
import com.woodpigeon.b3.Update._
import Fons._
import scala.language.implicitConversions

class AggregationTest extends AsyncFreeSpec {

  "EventList" - {

    "given a stream of events" - {
      val log = new InMemoryEventLog()
      log.stream("1234").append(
        AddNote("Hello"),
        AddNote("there"),
        AddNote("Jason!")
      )

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
