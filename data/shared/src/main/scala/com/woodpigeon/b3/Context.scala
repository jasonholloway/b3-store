// package com.woodpigeon.b3

// import scala.concurrent.Future
// import scala.concurrent.ExecutionContext.Implicits.global
// import EventSpan._
// import scala.util.{ Success, Try }

// class Context(source: LogSource, logSink: LogSink) {
//   private val cache = new LogCache()
//   private val staging = new LogCache()

//   private val fons = new Fons(logName => {
//     val combined = for {
//         fromUpstream <- source.read(logName, 0)
//         fromStaging <- Future(staging(logName).read())
//     } yield fromUpstream append fromStaging
//     combined.flatMap(Future.fromTry(_))
//   })

//   private val updater = new Updater(fons, new LogSink {
//     def append(logName: String, span: EventSpan): Try[Int] = span match {
//       case EventSpan.Empty() => Success(0)
//       case span@EventSpan.Full(_, _) => 
//         staging(logName).append(span)
//           .map { _ => span.end }
//     }

//     def commit(): Future[Unit] = ???
//   })

//   def view[E <: Entity :Behaviour](ref: Ref[E]) = fons.view(ref)
//   def write[E <: Entity :Behaviour, U](ref: Ref[E], update: U)(implicit wrap: U => RawUpdate) = updater.write(ref, update)
//   def save(): Future[Unit] = Future(())
// }


// class Updater(fons: Fons, log: LogSink) {
//   def write[E <: Entity, U](ref: Ref[E], update: U)(implicit behaviour: Behaviour[E], wrap: U => RawUpdate) : Future[E#View] =
//     for {
//       before  <- fons.view(ref)
//       after   <- Future(behaviour.update(before, update).getOrElse(throw new Error("Bad update!")))
//       _       <- behaviour.project(this, ref.name, before, after, update)
//       _       <- {
//         println("STARTING TO UPDATE...")
//         val logName = behaviour.name(ref.name)
//         val result = Future.fromTry(log.append(logName, EventSpan(wrap(update))))
//         println(s"UPDATED! name=$ref logName=$logName update=$update")
//         result
//       }
//     } yield after

// }
