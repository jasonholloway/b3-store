package com.woodpigeon.b3

import cats.kernel.Eq
import cats.kernel.laws.discipline.EqTests
import com.woodpigeon.b3.EventSpan._
import com.woodpigeon.b3.schema.v100.{ Event, PutProductDetails }
import org.scalacheck.Arbitrary
import org.scalacheck.rng.Seed
import org.scalatest.{ FunSuite, Matchers }
import cats.laws.discipline._
import cats.implicits._
import org.typelevel.discipline.scalatest.Discipline
import org.scalacheck._
import org.scalacheck.Arbitrary._
import scala.collection.immutable.SortedMap

class CtxSpec extends FunSuite with Matchers with Discipline {


  checkAll("Monad[Ctx]", MonadTests[Ctx].monad[Int, Int, Int])

  checkAll("Eq[Ctx[Int]]", EqTests[Ctx[Int]].eqv)

  checkAll("Eq[EventSpan]", EqTests[EventSpan].eqv)

  checkAll("Eq[Event]", EqTests[Event].eqv)

  implicit def arbEvent: Arbitrary[Event] = Arbitrary(
    PutProductDetails().asInstanceOf[RawUpdate].asInstanceOf[Event]
  )

  implicit def arbCtx[V](implicit arbVal: Arbitrary[V], arbStaging: Arbitrary[SortedMap[String, EventSpan]]): Arbitrary[Ctx[V]] = Arbitrary(
    for {
      v <- arbVal.arbitrary
      staging <- arbStaging.arbitrary
    } yield Ctx(v, staging)
  )

  implicit def eqEventSpan(implicit eqInt: Eq[Int], eqEvs: Eq[List[Event]]): Eq[EventSpan] = new Eq[EventSpan] {
    def eqv(x: EventSpan, y: EventSpan): Boolean = (x, y) match {
      case (Full(s1, e1), Full(s2, e2)) => eqInt.eqv(s1, s2) && eqEvs.eqv(e1, e2)
      case _ => false
    }      
  }

  implicit val eqEvent: Eq[Event] = new Eq[Event] {
    def eqv(a: Event, b: Event): Boolean = a.equals(b)
  }

  implicit def arbEventSpan: Arbitrary[EventSpan] = Arbitrary(
    Gen.oneOf[EventSpan](
      EventSpan.Empty(), 
      for {
        start <- Gen.posNum[Int]
      } yield EventSpan.Full(start, List())
    )
  )

  implicit def eqCtxV[V](implicit eqV: Eq[V], eqStaging: Eq[SortedMap[String, EventSpan]]): Eq[Ctx[V]] = new Eq[Ctx[V]] {
    def eqv(a: Ctx[V], b: Ctx[V]): Boolean = (a, b) match {
      case (Ctx(v1, s1), Ctx(v2, s2)) => eqV.eqv(v1, v2) && eqStaging.eqv(s1, s2)
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
