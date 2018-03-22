package com.woodpigeon.b3
import cats.Monad
import cats.Id
import cats.free.Free
import cats.kernel.Eq
import cats.laws.discipline.MonadTests
import cats.laws.discipline._
import com.woodpigeon.b3.schema.v100.PutProductDetails
import org.scalacheck.Arbitrary
import org.scalatest.{FunSuite,Matchers}
import org.scalacheck.Arbitrary._
import org.scalacheck._
import cats.~>
import org.typelevel.discipline.scalatest.Discipline
import scala.concurrent.Future

class ActionSpec extends FunSuite with Matchers with Discipline {
  import Action._

  checkAll("Monad[Transaction[Id, ?]]", MonadTests[Transaction[Id, ?]].monad[Int, Int, Int])

  implicit def arbTransaction[V]: Arbitrary[Transaction[Id, V]] = ???

  implicit def isoDummy: SemigroupalTests.Isomorphisms[Transaction[Id, ?]] = ???

  implicit def eqDummy[V]: Eq[V] = ???

  test("flump!") {
    val actions = for {
      haggis <- read(Ref.product("HAGGIS"))
      _ <- update(Ref.product("HAGGIS"), PutProductDetails("Krrumpt"))
    } yield ()

    val transaction = actions.toTransaction[Id]

    val result = transaction(new Store { })

    assert(result == true)
  }

  trait Store
  type Transaction[M[_], V] = Store => M[(Store, V)]

  implicit def interpret[M[_]]: (Action ~> Transaction[M, ?]) = new (Action ~> Transaction[M, ?]) {
   def apply[V](action: Action[V]): Transaction[M, V] = ??? 
  }

  implicit class RichActions[V](actions: Free[Action, V]) {
    def toTransaction[M[_] : Monad](implicit transform: Action ~> Transaction[M, ?], monad: Monad[Transaction[M, ?]]): Transaction[M, V]
        = actions.foldMap[Transaction[M, ?]](transform)(monad)
  }

  implicit def fakeTransactionMonad: Monad[Transaction[Id, ?]] = ???
  implicit def realTransactionMonad: Monad[Transaction[Future, ?]] = ???
}

