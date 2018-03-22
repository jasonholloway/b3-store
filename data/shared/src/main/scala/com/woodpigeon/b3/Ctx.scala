package com.woodpigeon.b3
import cats.kernel.Monoid
import cats.Monad
import com.woodpigeon.b3.{ Entity, Product }
import scala.annotation.tailrec
import scala.collection.immutable.SortedMap
import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }
import EventSpan._
import cats.syntax.monoid._
import cats.instances.string._
import cats.instances.sortedMap._
import cats.instances.try_._
import cats.syntax.traverse._
import scala.language.higherKinds

import Ctx._
case class Ctx[+V](fn: () => Try[(V, Staging)])

object Ctx {
  type Staging = SortedMap[String, EventSpan]
  type StagingCombo = Try[Staging]

  def apply[V](v: V): Ctx[V] = Ctx(() => Try((v, SortedMap())))

  implicit def ctxMonad: Monad[Ctx] = new Monad[Ctx] {

    def pure[V](v: V): Ctx[V] = Ctx(v)

    def flatMap[A, B](ctx: Ctx[A])(fn: A => Ctx[B]): Ctx[B] = 
      ctx match {
        case Ctx(fnPrev) => Ctx(() => fnPrev() match {
          case Success((v1, s1)) => fn(v1) match {
            case Ctx(fnNext) => fnNext() match {
              case Success((v2, s2)) =>
                Try(s1) |+| Try(s2) match {
                  case Success(s) => Success((v2, s))
                  case Failure(e) => Failure(e)
                }
              case Failure(e2) => Failure(e2)
            }
          }
          case Failure(e1) => Failure(e1)
        })
      }

    def tailRecM[A, B](a: A)(f: A => Ctx[Either[A, B]]): Ctx[B] =
      Ctx(() => {
        // @tailrec
        def loop(inp: Ctx[Either[A, B]], stagingCombo: StagingCombo): Try[(B, Staging)] = inp match {
          case Ctx(fn) => ???
        }

        loop(f(a), Success(SortedMap()))
      })
  }

  implicit def stagingComboMonoid: Monoid[StagingCombo] = new Monoid[StagingCombo] {
    def empty: StagingCombo = Success(SortedMap())
    def combine(a: StagingCombo, b: StagingCombo): StagingCombo = (a, b) match {
      case (Success(s1), Success(s2)) =>
        (s1.mapValues(Try(_)) |+| s2.mapValues(Try(_))).sequence[Try, EventSpan]
      case (e@Failure(_), _) => e
      case (_, e@Failure(_)) => e
    }
  }

  implicit class RefActions[E <: Entity](ref: Ref[E]) {
    def load: Ctx[Loaded[E]] = ???
    def append(update: RawUpdate): Ctx[Loaded[E]] = ???
  }

  implicit class RichCtx[V](ctx: Ctx[V]) {
    def runAs[F[_] : CtxRunner](store: Any)(implicit runner: CtxRunner[F]): F[V] 
      = runner.run(ctx, store)
  }
}

trait CtxRunner[F[_]] {
  def run[V](ctx: Ctx[V], store: Any): F[V] 
}

