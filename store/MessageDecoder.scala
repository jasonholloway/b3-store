package woodpigeon.bb.store

import io.circe.{Decoder, HCursor}
import io.circe.generic.auto._

object MessageDecoder extends Decoder[Message[_]] {
  implicit val updateDecoder = UpdateDecoder
  implicit val nopDecoder = NopDecoder

  override def apply(c: HCursor) = {
    c.downField("type").as[String].flatMap { bodyType =>
      val cursor = c.downField("body")
      val body = bodyType match {
        case "update" => cursor.as[Update[_]]
        case "nop" => cursor.as[Nop]
      }
      body.map(Message(_))
    }
  }
}

object UpdateDecoder extends Decoder[Update[_]] {
  override def apply(c: HCursor) = {
    c.downField("type").as[String].flatMap { opType =>
      val cursor = c.downField("op")
      val op = opType match {
        case "putProduct" => cursor.as[PutProduct]
        case "deleteProduct" => cursor.as[DeleteProduct]
      }
      op.map(Update(1, _))
    }
  }
}

object NopDecoder extends Decoder[Nop] {
  override def apply(c: HCursor) = Right(Nop())
}

