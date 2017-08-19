package woodpigeon.bb.store

import java.io.{InputStream, OutputStream}

import scala.io.Source
import com.amazonaws.services.lambda.runtime.Context
import io.circe.Json
import io.circe.parser._

class Handler {
  def handle(input: InputStream, output: OutputStream, context: Context ) = {
    parseInput.flatMap { json =>
      decodeMessage(json).map(_ match {
        case Message(nop: Nop) => ()
        case Message(update: Update[PutProduct]) => ()
        case _ => ()
      })
    }

    def parseInput = {
      val string = Source.fromInputStream(input).mkString
      parse(string)
    }

    def decodeMessage(json: Json) = {
      MessageDecoder.decodeJson(json)
    }
  }
}


case class UpdateResponse(ok: Boolean)

trait UpdateStore {
  def commit(update: Update[_])
}

trait UpdateRegistry {
  def getVersion: UpdateId
  def pushHead(id: UpdateId)
}

trait Lock {
  def release(): Unit
}


//
//    class UpdateHandler extends Lambda[Update, UpdateResponse] {
//
//      val store: UpdateStore = null
//      val registry: UpdateRegistry = null
//
//      def takeLock: Lock = ???
//
//      def getHead: UpdateId = ???
//
//      override def handle(update: Update): Either[Throwable, UpdateResponse] = {
//
//        Right(UpdateResponse(true))
//
////        val lock = takeLock
////
////        //instead of a lock, go for optimistic concurrency
////        //the initial check is just a sanity check to stop us performing work we don't have to do
////        //the important bit is at the end - the head pushing must be atomic. We need to see if SimpleDB
////        //offers atomicity. I've a feeling it will do.
////
////        try {
////          val version = registry.getVersion
////
////          val headId = getHead
////
////          if(batch.prevId != headId) {
////            return Left(new RuntimeException())
////          }
////
////          //check batch is of correct version - ie we've not been gazumped
////          //all batches will have a version number
////
////          store.save(batch)
////          registry.pushHead(batch.id)   //when this is pushed, the transaction is complete
////
////          Right(UpdateResponse(true))
////        }
////        finally {
////          lock.release()
////        }
//      }
//    }

