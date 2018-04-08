package com.woodpigeon.b3

import scala.collection.immutable.SortedMap
import scala.util.{Failure, Success, Try}

import Ctx._
import cats.implicits._
import cats.kernel.Eq
import cats.kernel.laws.discipline.{EqTests, MonoidTests}
import cats.laws.discipline._
import com.woodpigeon.b3.EventSpan._
import com.woodpigeon.b3.schema.v100.{AddNote, Event, PutProductDetails}
import org.scalacheck._
import org.scalacheck.Arbitrary._
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline

class CtxSpec extends FunSuite with Matchers with Discipline {


  checkAll("Eq[Ctx[Int]]", EqTests[Ctx[Int]].eqv)

  checkAll("Eq[EventSpan]", EqTests[EventSpan].eqv)

  checkAll("Eq[Event]", EqTests[Event].eqv)

  checkAll("Monoid[Try[Staging]]", MonoidTests[Try[Staging]].monoid)
  
  checkAll("Monad[Ctx]", MonadTests[Ctx].monad[Int, Int, Int])


  implicit def arbEvent: Arbitrary[Event] = Arbitrary(
    Gen.oneOf[Event](
      for {
        name <- Gen.alphaStr
      } yield RawUpdate.convert(PutProductDetails(name)).asEvent(),
      for {
        note <- Gen.alphaStr
      } yield RawUpdate.convert(AddNote(note)).asEvent()
    )
  )

  implicit def arbCtx[V](implicit arbVal: Arbitrary[V], arbStaging: Arbitrary[SortedMap[String, EventSpan]]): Arbitrary[Ctx[V]] = Arbitrary(
    Gen.frequency[Ctx[V]](
      (7, for {
        v <- arbVal.arbitrary
        staging <- arbStaging.arbitrary
      } yield Ctx(() => Success((v, staging)))),
      (1, for {
        errorText <- Gen.alphaStr
      } yield Ctx(() => Failure(new Throwable(errorText))))
    )
  )

  implicit def eqEventSpan(implicit eqInt: Eq[Int], eqEvs: Eq[List[Event]]): Eq[EventSpan] = new Eq[EventSpan] {
    def eqv(x: EventSpan, y: EventSpan): Boolean = (x, y) match {
      case (Full(s1, e1), Full(s2, e2)) => eqInt.eqv(s1, s2) && eqEvs.eqv(e1, e2)
      case (Empty(), Empty()) => true
      case _ => false
    }      
  }

  implicit def eqEvent: Eq[Event] = new Eq[Event] {
    def eqv(a: Event, b: Event): Boolean = a.equals(b)
  }

  implicit def arbEventSpan(implicit arbEvent: Arbitrary[Event]): Arbitrary[EventSpan] = Arbitrary(
    Gen.oneOf[EventSpan](
      Empty(), 
      for {
        start <- Gen.posNum[Int]
        events <- Gen.listOf(arbEvent.arbitrary)
      } yield Full(start, events)
    )
  )

  implicit def eqThrowable: Eq[Throwable] = Eq.by[Throwable, String](_.toString)

  implicit def eqCtxV[V](implicit eqFn: Eq[() => Try[(V, Staging)]]): Eq[Ctx[V]] = new Eq[Ctx[V]] {
    def eqv(a: Ctx[V], b: Ctx[V]): Boolean = (a, b) match {
      case (Ctx(fn1), Ctx(fn2)) => eqFn.eqv(fn1, fn2)
    }
  }

  implicit def arbFunEventSpanIdentity(implicit arbSpan: Arbitrary[EventSpan]): Arbitrary[EventSpan => EventSpan] = Arbitrary(
    arbSpan.arbitrary.map(v => (_: EventSpan) => v)
  )

  // implicit def cogenCtx[V](implicit cogenV: Cogen[V], cogenMap: Cogen[Map[String, EventSpan]]): Cogen[Ctx[V]] = Cogen((seed: Seed, t: Ctx[V]) => t match {
  //   case Ctx(value, staged) => cogenMap.perturb(cogenV.perturb(seed, value), staged)
  // })

  implicit def arbFun[V](implicit arbV: Arbitrary[V]): Arbitrary[V => V] = Arbitrary(
    arbV.arbitrary.map(v => (_: V) => v)
  )

} 
