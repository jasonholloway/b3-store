package com.woodpigeon.b3
import cats.Monad
import scala.collection.immutable.SortedMap

case class Ctx[+V](value: V, staged: SortedMap[String, EventSpan] = SortedMap())

object Ctx {
  implicit def ctxMonad: Monad[Ctx] = new Monad[Ctx] {
    def pure[V](v: V): Ctx[V] = Ctx(v)
    def flatMap[A, B](ctx: Ctx[A])(fn: A => Ctx[B]): Ctx[B] = ???
    def tailRecM[A, B](a: A)(f: A => Ctx[Either[A, B]]): Ctx[B] = ???
  }
}

