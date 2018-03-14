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

sealed trait Ctx[+V]
case class CtxVal[+V](value: V, staged: SortedMap[String, EventSpan] = SortedMap()) extends Ctx[V]
case class CtxErr[+V](err: Throwable) extends Ctx[V]


object Ctx {
  type Staging = SortedMap[String, EventSpan]
  type StagingCombo = Try[Staging]

  def apply(): Ctx[Unit] = CtxVal(Unit)

  implicit def ctxMonad: Monad[Ctx] = new Monad[Ctx] {
    def pure[V](v: V): Ctx[V] = CtxVal(v)
    def flatMap[A, B](ctx: Ctx[A])(fn: A => Ctx[B]): Ctx[B] =
      ctx match {
        case CtxErr(err) => CtxErr(err)
        case CtxVal(v1, s1) =>
          fn(v1) match {
            case CtxErr(err) => CtxErr(err)
            case CtxVal(v2, s2) => {
              Try(s1) |+| Try(s2) match {
                case Success(s) => CtxVal(v2, s)
                case Failure(e) => CtxErr(e)
              }
            }
          }
      }

    def tailRecM[A, B](a: A)(f: A => Ctx[Either[A, B]]): Ctx[B] = {
      @tailrec
      def loop(inp: Ctx[Either[A, B]], stagingCombo: StagingCombo): Ctx[B] = inp match {
        case CtxVal(Right(v), staging) => 
          Try(staging) |+| stagingCombo match {
            case Success(s) => CtxVal(v, s)
            case Failure(e) => CtxErr(e)
          }
        case CtxVal(Left(v), staging) => {
          loop(f(v), Try(staging) |+| stagingCombo)
        }
        case CtxErr(e) => CtxErr(e)
      }

      loop(f(a), Success(SortedMap()))
    }
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

