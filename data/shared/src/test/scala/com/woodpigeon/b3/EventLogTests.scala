// package com.woodpigeon.b3

// import cats.implicits._
// import com.woodpigeon.b3.RawUpdate._
// import com.woodpigeon.b3.schema.v100._
// import org.scalatest.{AsyncFreeSpec, FreeSpec}
// import scala.async.Async.{async, await}
// import scala.language.implicitConversions
// import EventSpan._
// import scala.util.Failure

// class LogCacheTests extends FreeSpec {

//   implicit class RichEvent(event: Event) {
//     def is(update: RawUpdate) = event == update.asEvent
//   }

//   private def newUpdates(words: String*): Seq[RawUpdate] =
//     words.map(w => RawUpdate.convert(AddNote(w)))

//   "LogCache" - {
//     val cache = new LogCache()

//     "stores and regurgitates updates" in {
//       val updates = newUpdates("Hello", "Jason!")

//       cache("log1").append(EventSpan(updates:_*))

//       cache("log1").read() match {
//         case Full(start, events) => {
//             assert(start == 0)
//             assert(events(0) is updates(0))
//             assert(events(1) is updates(1))
//         }
//         case _ => fail
//       }
//     }

//     "refuses to append if offset doesn't match" in {
//       cache("log1").append(EventSpan(start = 0, AddNote("Blahhh")))

//       cache("log1").append(EventSpan(start = 10, AddNote("Wibble"))) match {
//         case Failure(_) => succeed
//         case _ => fail
//       }
//     }

//     "returns Empty as default" in {
//       cache("giraffe").read() match {
//         case Empty() => succeed
//         case _ => fail
//       }
//     }

//   }

// }



// class EventLogTests extends AsyncFreeSpec {

//   def test[L <: EventLog](name: String, newLog: () => L) : Unit = {
//     s"$name" - {

//       "should allow writes" in async {
//         val log = newLog()

//         val fragments = Seq(
//           StreamFragment("TEST:123", Seq[RawUpdate](
//             AddNote("Hello"), AddNote("there"), AddNote("Jason!")
//           ))
//         )

//         await { log.write(StreamFragmentBatch(fragments)) }

//         assert(true)
//       }

//       "roundtrips happily" in async {
//         val log = newLog()

//         val batch = StreamFragmentBatch(Seq(
//           StreamFragment("TEST:123", Seq[RawUpdate](
//             AddNote("Hello"), AddNote("there"), AddNote("Jason!")
//           ))
//         ))

//         await { log.write(batch) }

//         val offsetMap = StreamOffsetMap(Map("TEST:123" -> 0))

//         val returned = await { log.read(offsetMap) }

//         val phrase = returned.fragments.head.events
//             .map { _.inner }
//             .flatMap {
//               case Event.Inner.AddNote(AddNote(message)) => Some(message)
//               case _ => None
//             }.mkString(" ")

//         assert(phrase == "Hello there Jason!")

//         assert(returned.fragments.head.ref == "TEST:123")
//       }


//       "successive commits are concatenated" in async {
//         val log = newLog()

//         val batch1 = StreamFragmentBatch(Seq(
//           StreamFragment("TEST:123", Seq[RawUpdate](
//             AddNote("Hello")
//           ))
//         ))

//         await { log.write(batch1) }

//         val batch2 = StreamFragmentBatch(Seq(
//           StreamFragment("TEST:123", Seq[RawUpdate](
//             AddNote("there"), AddNote("Jason!")
//           ))
//         ))

//         await { log.write(batch2) }

//         val offsetMap = StreamOffsetMap(Map("TEST:123" -> 0))

//         val returned = await { log.read(offsetMap) }

//         val phrase = returned.fragments.flattenUpdates
//                       .flatMap {
//                         case RawUpdate(AddNote(message))
//                           => Some(message)
//                         case _ => None
//                       }.mkString(" ")

//         assert(phrase == "Hello there Jason!")
//       }

//     }
//   }

//   test("InMemoryEventLog", () => new InMemoryEventLog())


//   implicit class EnrichedFragments(fragments: Seq[StreamFragment]) {
//     def flattenUpdates: Seq[RawUpdate]
//       = fragments.flatMap(_.events).toList.traverse(_.asUpdate).get

//   }


// }
