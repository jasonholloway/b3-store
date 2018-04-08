// package com.woodpigeon.b3

// import scala.collection.mutable
// import EventSpan._
// import scala.util.Try

// class LogCache {
//   private val logMap = mutable.Map[String, Log]()

//   def apply(logName: String): Log = synchronized {
//     println(s"ACCESSING $logName from $this -> $logMap")
//     logMap.getOrElseUpdate(logName, new Log())
//   }
// }


// class Log {
//   private var updates: EventSpan = empty

//   def append(span: EventSpan): Try[Unit] = synchronized {
//     updates.append(span)
//       .map {
//         updates = _
//       }
//       .map { _ =>
//         println(s"APPENDED! $span")
//       }
//   }

//   def read() : EventSpan = {
//     println(s"READING! $updates")
//     updates
//   }

//   def clear(): Unit = { updates = empty }
// }
