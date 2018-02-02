package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100._

import scala.collection.mutable
import scala.concurrent.Future
import scala.util.Try

trait EventLog {
  def read(map: StreamOffsetMap) : Future[StreamFragmentBatch]
  def write(payload: StreamFragmentBatch) : Future[Unit]
}



trait LogSource {
  def read(logName: String, offset: Int): Future[LogSpan]
}

trait LogSink {
  def append(logName: String, updates: LogSpan): Try[Int]
  def commit(): Future[Unit]
}

//
//class Staging(logSource: LogSource, logSink: LogSink) extends LogSource with LogSink {
//
//  private val localLogs = mutable.Map[String, LocalLog]()
//
//  def read(logName: String, offset: Int): Future[Seq[RawUpdate]] = ???
//  //read from local and also remote
//
//  def append(logName: String, updates: LogSpan): Int = {
//    val log = getLocalLog(logName)
//    log.append(updates)
//    log.offset + log.updates.length
//  }
//
//  def commit(): Future[Unit] = logSink.commit()
//
//
//  private def getLocalLog(logName: String) =
//    synchronized { localLogs.getOrElseUpdate(logName, new LocalLog(0)) }
//
//  private class LocalLog(val offset: Int) {
//    private val log = mutable.Queue[RawUpdate]()
//
//    def append(updates: Seq[RawUpdate]) =
//      synchronized { log ++= updates }
//
//    def updates: Seq[RawUpdate] = log
//  }
//
//}


//but the fons will have staging, the cache, and the ultimate logSource to read from
//the choice is between having each layer do its own little bit, offering an increasingly benign
//interface to the consumer, or a single cross-cutting method of fetching that is all above board

//layering hides lots but makes the eventual usage clean, perhaps too clean
//

//at the top level, we have two cross-cutting operations
//
//the first is reading aggregates, which will be done by applying events each time
//the overall flow of this is to be presented at the top level
//
//how will we know we are up to date? we'll trust that our cache is done
//and some secondary process will pull updates into the cache out of band
//by occasionally polling a Dynamo record
//
//so we don't have to worry about being up to date.

//on reading, we will consult the cache, load the cache if it is empty,
//and then read from staging
//
//staging might have stuff in it, it might not
//there will be a responsibility to make sure that staging
//
//
//it feels like staging shouldn't hold much behaviour, or should it?
//
//
//Staging shouldn't be taking logSource: though it should take logSink
//as its raison d'etre is indeed to flush to storage.
//
//there should also be a logCache underneath Staging
//when staging commits, on successful flush, it should
//also write to the cache. So logCache is itself a logsink.
//
//if you flush staging, then the cache is appended with those events,
//with the expectation that previous bits of the cache have already been filled in.
//
//how could staging ever put tot he cache without preceding cached itemsbeing inplace
//the problem with this set-up is that it represents special extra-modular information.
//
//cross-modular reliance, strings of clammy dependency across domains.
//
//so Staging would append to the cache as a nicety (would it even know about it?)
//but on appending, it would only be committing what itself knows, with no requirement that anything previous has been populated.
//
//So the cache, on receiving a tranche from wherever (the cache just commits whatever tranches it is given, and exposes its current coverage) -
//on receipt it stores its tranches. But the interface of the cache would so much simpler if it was taken for granted that the cache were always populated front-to-back.

//With such a limited interface, we'd have to defensively raise errors if our constraints were broken.
//i.e. the cache wouldn't append anything if its versions mismatched.

//So, the cache just stores in its log, and expects all inputs to be nicely provided in series.
//How is this different from our in-memory log? It's just a repurposed in-memory log, taking as its inputs numbered tranches.

//The in-memory log would then enforce that events matched the current cursors.
//Staging could also be a log - but then the contract would have to be that if the log were empty, we could start from any version
//but if something were in, appendages would have to follow nicely.



