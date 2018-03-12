package com.woodpigeon.b3
import cats.{ Monad, Traverse }
import scala.annotation.tailrec
import scala.collection.immutable.SortedMap
import scala.util.{ Failure, Success, Try }
import EventSpan._
import cats.syntax.monoid._
import cats.instances.string._
import cats.instances.sortedMap._
import cats.instances.try_._
import cats.syntax.traverse._

sealed trait Ctx[+V]
case class CtxVal[+V](value: V, staged: SortedMap[String, EventSpan] = SortedMap()) extends Ctx[V]
case class CtxErr[+V](err: Throwable) extends Ctx[V]


object Ctx {
  type Staging = SortedMap[String, EventSpan]

  implicit def ctxMonad: Monad[Ctx] = new Monad[Ctx] {
    def pure[V](v: V): Ctx[V] = CtxVal(v)

    def flatMap[A, B](ctx: Ctx[A])(fn: A => Ctx[B]): Ctx[B] =
      ctx match {
        case CtxErr(err) => CtxErr(err)
        case CtxVal(v1, staging1) =>
          fn(v1) match {
            case CtxErr(err) => CtxErr(err)
            case CtxVal(v2, staging2) => {
              val combined = staging1.mapValues(Try(_)) |+| staging2.mapValues(Try(_))
              val sequenced = combined.sequence[Try, EventSpan]

              sequenced match {
                case Success(s) => CtxVal(v2, s)
                case Failure(e) => CtxErr(e)
              }
            }
          }
      }

    def tailRecM[A, B](a: A)(f: A => Ctx[Either[A, B]]): Ctx[B] = {
      // @tailrec
      // def loop(inp: Ctx[Either[A, B]], stagings: List[Staging]): Ctx[B] = inp match {
      //   case CtxVal(Right(v), staging) => CtxVal(v, (staging :: stagings).combineAll)
      //   case CtxVal(Left(v), staging) => {
      //     val combinedStagings = staging :: stagings //this could return failure - should match against it
      //     loop(f(v), combinedStagings)
      //   }
      // }

      // loop(f(a), Nil)
      ???
    }
  }
}

//the Try of the EventSpan compositions should be sucked up into the Ctx monad
//a Ctx monad can therefore have another state: one of error.
//more things will be packed into the Ctx, to absolve our code of its gnarly sins

