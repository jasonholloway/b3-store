import com.woodpigeon.b3._
import com.woodpigeon.b3.schema.v100.{AddNote, Event, _}
import org.scalatest.AsyncFreeSpec
import com.woodpigeon.b3.Update._
import Updaters._
import Creators._

import scala.language.implicitConversions

class AggregationTest extends AsyncFreeSpec {

  "StreamFragment" - {

    "given a stream of events" - {
      val log = new InMemoryEventLog()
      log.stream("1234").append(
        AddNote("Hello"),
        AddNote("there"),
        AddNote("Jason!")
      )

      "should collect all those events" in {
        val fons = new Fons(log)
        fons.view[StreamFragment]("1234")
          .map(aggr => {
            assert(aggr.events.lengthCompare(3) == 0)

            val phrase = aggr.events.flatMap {
              case Event(Event.Inner.AddNote(AddNote(message))) => Some(message)
              case _ => None
            }.mkString(" ")

            assert(phrase == "Hello there Jason!")
          })
      }

    }
  }


  "ProductSet collects all created products" in {
    val log = new InMemoryEventLog()
    log.stream("666").append(
      new PutProductDetails("p123", "Velvet Socks", 0.50f)
    )

    log.stream("667").append(
      new PutProductDetails("p456", "Hair Shirt", 13.33f)
    )

    val fons = new Fons(log)
    fons.view[ProductSet]("products")
        .map(productSet => {
          assert(productSet.skus == Seq("p123", "p456"))
        })
  }


//
//  "ProductInfo" - {
//    val log = new InMemoryEventLog()
//
//    log.stream("1234").append()
//
//    val fons = new Fons(log)
//    val info = fons.viewAs[ProductInfo]("1234", "ProductInfo")
//
//    ???
//  }

}
