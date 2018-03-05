package com.woodpigeon.b3

import cats.kernel.Eq
import cats.instances.TupleInstances
import org.scalatest.{ FunSuite, Matchers }
import cats.laws.discipline._
import cats.implicits._
import cats._
import org.typelevel.discipline.scalatest.Discipline
import org.scalacheck._



class CtxSpec extends FunSuite with Matchers with Discipline {

  checkAll("Monad[Ctx]", MonadTests[Ctx].monad[Int, Int, Int])

  implicit def arbCtx[V](implicit arbVal: Arbitrary[V]): Arbitrary[Ctx[V]] = Arbitrary(
    arbVal.arbitrary.map(Ctx(_))
  )

  implicit def eqCtx[V](implicit eqVal: Eq[V]): Eq[Ctx[V]] = new Eq[Ctx[V]] {
    def eqv(x: Ctx[V], y: Ctx[V]): Boolean = true
  }

}
