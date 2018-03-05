package com.woodpigeon.b3
import cats.Monad

case class Ctx[+V](value: V, staged: Map[String, EventSpan] = Map())

object Ctx {
  implicit def ctxMonad: Monad[Ctx] = new Monad[Ctx] {
    def pure[V](v: V): Ctx[V] = Ctx(v)
    def flatMap[A, B](ctx: Ctx[A])(fn: A => Ctx[B]): Ctx[B] = ???
    def tailRecM[A, B](a: A)(f: A => Ctx[Either[A, B]]): Ctx[B] = ???
  }
}

