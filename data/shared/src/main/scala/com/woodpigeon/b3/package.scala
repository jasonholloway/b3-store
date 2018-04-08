package com.woodpigeon

import scala.collection.immutable.SortedMap

package object b3 {
  type Log = Vector[RawUpdate]
  type LogMap = SortedMap[String, Log]
}
