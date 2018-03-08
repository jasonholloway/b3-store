package com.woodpigeon.b3
import cats.Monad
import scala.collection.immutable.SortedMap

case class Ctx[+V](value: V, staged: SortedMap[String, EventSpan] = SortedMap())

object Ctx {
  implicit def ctxMonad: Monad[Ctx] = new Monad[Ctx] {
    def pure[V](v: V): Ctx[V] = Ctx(v)

    def flatMap[A, B](ctx: Ctx[A])(fn: A => Ctx[B]): Ctx[B] = ctx match {
      case Ctx(v1, staging1) =>
        fn(v1) match {
          case Ctx(v2, staging2) => Ctx(v2, staging1 ++ staging2)
        }
    }

    def tailRecM[A, B](a: A)(f: A => Ctx[Either[A, B]]): Ctx[B] = ???
  }
}

