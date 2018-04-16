package com.woodpigeon.b3

import cats.Functor
import com.woodpigeon.b3.schema.v100.{ AddNote, PutProductDetails }
import org.scalacheck.{ Arbitrary, Gen }
import org.scalacheck.Arbitrary._
import scala.math.BigDecimal
import scala.language.implicitConversions

object Arbitraries {
  implicit def arbPutProductDetails: Arbitrary[PutProductDetails] = Arbitrary(
    for {
      name <- Gen.alphaStr
      price <- arbitrary[BigDecimal]
    } yield PutProductDetails(name, price.toFloat) 
  )

  implicit def arbAddNote: Arbitrary[AddNote] = Arbitrary(
    for {
      note <- Gen.alphaStr
    } yield AddNote(note)
  )
  
  implicit def arbRawUpdate: Arbitrary[RawUpdate] = Arbitrary(
    Gen.oneOf(
      arbitrary[PutProductDetails],
      arbitrary[AddNote]
    )
  )

  implicit def arbLog: Arbitrary[Log] = Arbitrary(
    Gen.listOf(arbitrary[RawUpdate]).map(_.toVector)
  )


  implicit def functorConverter[F[_]: Functor, A, B](fa: F[A])(implicit F: Functor[F], a2b: A => B): F[B] =
    F.map(fa)(a2b)

  implicit def genFunctor: Functor[Gen] = new Functor[Gen] {
    override def map[A, B](fa: Gen[A])(f: A => B): Gen[B] = fa.map(f)
  }
}

