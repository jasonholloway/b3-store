package com.woodpigeon.b3

import cats.free.Free
import com.woodpigeon.b3.Behaviour
import scala.language.implicitConversions

import cats.{~>, Functor}
import cats.data._
import cats.kernel.Eq
import cats.kernel.laws.discipline.EqTests
import com.woodpigeon.b3.Transaction._
import com.woodpigeon.b3.schema.v100.{AddNote, PutProductDetails}
import org.scalacheck._
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline

class ActionSpec extends FunSuite with Matchers with Discipline {
  import Action._

  val compileToLogActions: Action ~> Free[LogAction, ?] =
    λ[Action ~> Free[LogAction, ?]]({
      case Read(ref) => 
        Free.liftF(LogAction.Read(ref))
          .map(aggregateView(ref, _))
      case Update(ref, events) => 
        Free.liftF(LogAction.Append(ref))
          .map(aggregateView(ref, _))
    })

  def aggregateView[E <: Entity](ref: Ref[E], log: Log): E#View =
    log.foldLeft(ref.behaviour.create(ref.name)) {
      (ac, ev) => ref.behaviour.update(ac, ev).getOrElse(ac)
    }

  implicit def compileTransaction: Action ~> Transaction =
    λ[Action ~> Transaction]({
      case Read(ref) => {
        ???
      }
      case Update(ref, events) => {
        ???
      }
    })

  //the below is what we will swap about between test and live

  implicit def executeTransaction: Transaction ~> FakeStore = 
    λ[Transaction ~> FakeStore]({
      case Load(name, offset) => ???
      case Commit(name, events) => ???
    })

  val actions = for {
    a <- Action.read(Ref.product("hello"))
    _ <- Action.update(Ref.product("boo"), PutProductDetails("flimflam", 13.1F))
  } yield ()

  val transaction = actions.compile[Transaction](compileTransaction)
  
  val fakeStoreState = transaction.foldMap(executeTransaction)
  val again = fakeStoreState
    .flatMap(_ => transaction.foldMap(executeTransaction))

  import cats.instances.vector._
  import cats.instances.sortedMap._
  import cats.instances.string._
  import org.scalacheck.Arbitrary._

  type FakeStore[V] = State[LogMap, V]

  checkAll("Eq[Log]", EqTests[Log].eqv)
  checkAll("Eq[LogMap]", EqTests[LogMap].eqv)

  implicit def eqRawUpdate: Eq[RawUpdate] = new Eq[RawUpdate] {
    def eqv(a: RawUpdate, b: RawUpdate): Boolean = a.equals(b)
  }

  implicit def arbRawUpdate: Arbitrary[RawUpdate] = Arbitrary(
    Gen.oneOf(
      arbitrary[PutProductDetails],
      arbitrary[AddNote]
    )
  )

  implicit def arbLog: Arbitrary[Log] = Arbitrary(
    Gen.listOf(arbitrary[RawUpdate]).map(_.toVector)
  )

  implicit def cogen[V]: Cogen[V] = Cogen(v => 13)

  implicit def arbPutProductDetails: Arbitrary[PutProductDetails] = Arbitrary(
    for {
      name <- Gen.alphaStr
      price <- arbitrary[BigDecimal]
    } yield PutProductDetails(name, price.toFloat) 
  )

  implicit def arbAddNote: Arbitrary[AddNote] = Arbitrary(
    for {
      note <- Gen.alphaStr
    } yield AddNote(note)
  )
  

  implicit def functorConverter[F[_]: Functor, A, B](fa: F[A])(implicit F: Functor[F], a2b: A => B): F[B] =
    F.map(fa)(a2b)

  implicit def genFunctor: Functor[Gen] = new Functor[Gen] {
    override def map[A, B](fa: Gen[A])(f: A => B): Gen[B] = fa.map(f)
  }
}
