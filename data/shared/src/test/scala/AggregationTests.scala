import com.woodpigeon.b3.{Fons, InMemoryEventLog, Sink}
import com.woodpigeon.b3.schema.v100._
import com.woodpigeon.b3.Sink._
import org.scalatest.{AsyncFreeSpec}
import com.woodpigeon.b3.Updates._
import Fons._

import scala.async.Async.{async, await}
import scala.language.implicitConversions

class AggregationTests extends AsyncFreeSpec {

  "EventList" - {

    "given a stream of events" - {

      val log = new InMemoryEventLog()
      val sink = new Sink(log)

      sink.commit("1234", Seq(
        AddNote("Hello").asEvent(0),
        AddNote("there").asEvent(1),
        AddNote("Jason!").asEvent(2)
      ))
      //should inject these into log, to served from Fons

      "should collect all those events" in async {
        val fons = new Fons(log)

        val aggr = await { fons.viewAs[EventList]("1234", "EventList") }

        assert(aggr.events.length == 3)

        val phrase = aggr.events.flatMap {
                        case Event(_, Event.Inner.AddNote(AddNote(message))) => Seq(message)
                        case _ => Nil
                      }.mkString(" ")

        assert(phrase == "Hello there Jason!")
      }


    }
  }

}
