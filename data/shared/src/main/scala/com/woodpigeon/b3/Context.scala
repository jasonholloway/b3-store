package com.woodpigeon.b3

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class Context(logSource: LogSource, logSink: LogSink) {
  private val cache = new LogCache()
  private val staging = new LogCache()

  private val fons = new Fons(new LogSource {
    def read(logName: String, offset: Int): Future[LogSpan] = {
      Future(staging(logName).read()) //and offset to be taken into account

      //go to staging
      //go to cache
      //go to source
    }
  })

  private val updater = new Updater(fons, new LogSink {
    def append(logName: String, updates: LogSpan): Try[Int]
      = staging(logName).append(updates)
          .map { _ => updates.end }

    def commit(): Future[Unit] = ???
  })

  def view[E <: Entity :Behaviour](ref: Ref[E]) = fons.view(ref)
  def write[E <: Entity :Behaviour, U](ref: Ref[E], update: U)(implicit wrap: U => RawUpdate) = updater.write(ref, update)
  def save(): Future[Unit] = ???
}


class Updater(fons: Fons, log: LogSink) {
  def write[E <: Entity, U](ref: Ref[E], update: U)(implicit behaviour: Behaviour[E], wrap: U => RawUpdate) : Future[E#View] =
    for {
      before  <- fons.view(ref)
      after   <- Future(behaviour.update(before, update).getOrElse(throw new Error("Bad update!")))
      _       <- behaviour.project(this, ref.name, before, after, update)
      _       <- Future.fromTry(log.append(behaviour.name(ref.name), LogSpan(0, List(wrap(update)))))
    } yield after



}
