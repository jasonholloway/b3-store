package com.woodpigeon.b3
import cats.Monad
import cats.Id
import cats.free.Free
import cats.kernel.{ Eq }
import cats.laws.discipline.MonadTests
import cats.kernel.laws.discipline.EqTests
import com.woodpigeon.b3.schema.v100.PutProductDetails
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalatest.{FunSuite,Matchers}
import org.scalacheck.Arbitrary._
import org.scalacheck._
import cats.~>
import org.typelevel.discipline.scalatest.Discipline
import cats.instances.int._
import cats.instances.tuple._

class ActionSpec extends FunSuite with Matchers with Discipline {
  import Action._

  checkAll("Eq[Transaction[FakeStore, Int]]", EqTests[Transaction[FakeStore, Int]].eqv)

  checkAll("Monad[Transaction[FakeStore, ?]]", MonadTests[Transaction[FakeStore, ?]].monad[Int, Int, Int])


  implicit def cogenStore: Cogen[FakeStore] = Cogen(_ => 13) //!!!!!!!!

  implicit def arbStore: Arbitrary[FakeStore] = Arbitrary(new FakeStore(Map())) //!!!!!!!!!!!!!!!!!!!!!!1

  implicit def eqStore: Eq[FakeStore] =
    Eq.instance((s1: FakeStore, s2: FakeStore) => true)

  implicit def eqTransaction[V](implicit eqStore: Eq[FakeStore], eqV: Eq[V]): Eq[Transaction[FakeStore, V]] =
    Eq.instance((t1: Transaction[FakeStore, V], t2: Transaction[FakeStore, V]) => {
      Gen.listOfN(500, arbitrary[FakeStore])
        .map(_.forall(store => {
          val (store1, v1) = t1(store)
          val (store2, v2) = t2(store)
          Eq[FakeStore].eqv(store1, store2) && Eq[V].eqv(v1, v2)
        }))
        .sample.getOrElse(false)
    })


  case class FakeStore(logs: Map[String, Any]) extends Store {
    type Result[V] = Id[V]
  }


  test("flump!") {
    val actions = for {
      haggis <- read(Ref.product("HAGGIS"))
      _ <- {
        update(Ref.product("HAGGIS"), PutProductDetails("Krrumpt"))
      }
    } yield haggis.view.name

    val transaction = actions.toTransaction[FakeStore]

    val result = transaction(new FakeStore(Map()))

    assert(result == true)
  }

  trait Store { type Result[_] }
  type Transaction[S <: Store, V] = S => S#Result[(S, V)]

  implicit def interpret[S <: Store]: (Action ~> Transaction[S, ?]) = new (Action ~> Transaction[S, ?]) {
   def apply[V](action: Action[V]): Transaction[S, V] = ??? 
  }

  implicit class RichActions[V](actions: Free[Action, V]) {
    def toTransaction[S <: Store](implicit transform: Action ~> Transaction[S, ?], monad: Monad[Transaction[S, ?]]): Transaction[S, V]
        = actions.foldMap[Transaction[S, ?]](transform)(monad)
  }

  implicit def transactionMonad[S <: Store]: Monad[Transaction[S, ?]] = new Monad[Transaction[S, ?]] {
    type T[V] = Transaction[S, V]

    def pure[V](v: V): T[V] =
      (store: S) => S#Result(store, v)

    def flatMap[A, B](a: T[A])(fn: A => T[B]): T[B] = 
      (store1: S) => {
        val (store2, v2) = a(store1)
        val (store3, v3) = fn(v2)(store2)
        S#Result(store3, v3) //stores should be merged here
      }

    def tailRecM[A, B](a: A)(f: A => T[Either[A, B]]): T[B] =
      ???
  }

  case class LiveStore() extends Store
}

