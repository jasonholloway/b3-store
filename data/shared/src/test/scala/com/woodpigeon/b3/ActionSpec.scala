package com.woodpigeon.b3

import cats.free.Cofree
import cats.laws.ComonadLaws
import cats.laws.discipline.ComonadTests
import cats.{ Comonad, ~> }
import cats.data._
import cats.free.Free
import cats.instances.sortedMap._
import cats.instances.string._
import cats.instances.vector._
import cats.kernel.Eq
import cats.kernel.laws.discipline.EqTests
import com.woodpigeon.b3.Transaction._
import com.woodpigeon.b3.schema.v100.{ProductView, PutProductDetails}
import org.scalacheck._
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.Checkers
import org.typelevel.discipline.scalatest.Discipline
import cats.instances.tuple._
import cats.instances.int._

class ActionSpec extends FunSuite with Matchers with Discipline with Checkers {
  import Behaviours._
  import Arbitraries._

  type FakeStore[V] = State[LogMap, V]

  checkAll("Eq[Log]", EqTests[Log].eqv)
  checkAll("Eq[LogMap]", EqTests[LogMap].eqv)
  
  //And now, again: more laws! A restatement of previously-stated ones!
  //
  //so, firstly:
  //Action ~> LogAction
  //
  //there should be an entity cache at this stage
  //but the core behaviour, within a single structure,
  //is for each requested entity to result in a loading of logs for that entity
  //then, too, if there's an intervening update between reads of the same entity
  //we want the log to be immediately communicated upwards (as our monad may be deconstructed, branched)
  //
  //*** An Update is immediately passed upwards *** - basic law
  
  //but reads may be supressed... 
  //in fact, it may be that there is only ever one Read accessed per structure.  
  //but what of small subsections? We want our laws to always make, in every eventuality.

  //well... our interpreter isn't stateless, if we're maintaining a cache between participles.
  //in which case, we'd test our cache state about. Our Free would become a State monad returning another Free
  //but this State monad would itself be intermediate, yielding up a Free monad of LogActions
  //but its exposure as State would allow invariants to be enforced by type.

  //Free[Action, ?] ~> StateT[Free[LogAction, ?], EntityMap, ?]
  //Free[Action, ?] ~> Free[LogAction, ?] (via the above state monad translation)
  //this transform via state still has to satisfy laws... how do you assert that the structure remains the same?
  //dunno - but yes, it still has to obey the laws we are now forming

  //Free[UpdateEntity, V] ~> Free[AppendLog, V]
  //there's absolutely no doubt about this. We want to publish our update immediately, whatever intermediate stages ofprocesing there may be.

  //In a given section of monad, each GetEntity should result in at least one ReadLog
  //though this will be broken if state is involved (but it isn't involved! the joint is at the edge of interpretation)
  //the scope of the stateful cache breaks at the limit of each pass of interpretation into Free[LogAction, ?]

  //But but but... if we have state in place, it seems a shame to throw it away. But maybe we are forced to shun it reasonably.

  //When we move into a structure of LogActions, we are really finalising everything for higher consumption.
  //The higher consumer - will they be interested in shunting together two monoidal LogAction structures?
  //Maybe they would be, but if we were keeping state about, there would be no structure to bare, as the actual layout of LogActions
  //would be lazily determined.
  //Two lots of application-level Actions could be cut and shut, but as soon as you start optimising (or, in effect, executing) you
  //have to relinquish the tranquility of your starting point. The transitivities (what I mean is, the universal laws) of you original program
  //crystallise into a less abstract realisation, a material singularity, an executed action (though this action too operates in a field of trade and laws - runtime serialization!)

  //--------------------------------------------------

  //but this idea of publishing upwards immediately is broken at the Transaction layer. But so - so, it should be so, exactly so. Oh yes.

  //*** each LoadEntity should have at least one ReadLog
  //*** each LoadEntity should have exactly *one* ReadLog - this enforces entity cacheing
  //*** a SpyEntity receives all the events we'd expect
  //*** an UpdateEntity becomes an AppendLog (and perhaps others) directly
  //*** projections are also appended
  //*** projections and updates should also update the entity cache
  //these should be being noted down in tests as we go, instead of whittlin' about em like.

  //the simplest solution should of course be to have no cacheing whatsoever - this is our priority
  //
  //so, just a step past the simplest 'positive' laws are those that in spirit only protect against the misbehaviour of the yet-to-be-implemented cache,
  import Compilation._
  import Arbitrary._

  case class Translation[From[_], To[_], V](from: Free[From, V], to: Free[To, V])

  type History = List[Int]
  type TracedFree[F[_], V] = StateT[Free[F, ?], History, V]

  def trace[F[_]]: Free[F, ?] ~> TracedFree[F, ?] =
    λ[Free[F, ?] ~> TracedFree[F, ?]](StateT.lift(_))

  def compileActionsWithTrace[V](actions: Free[Action, V]): TracedFree[LogAction, V] =
    actions
      .foldMap(compileActions.andThen(trace))
    
  def compileLogActionsWithTrace[V](actions: Free[Action, V])(implicit co: Comonad[Free[LogAction, ?]]): TracedFree[Transaction, V] =
    compileActionsWithTrace(actions)
      .transformF { fa =>
        fa.mapK(compileLogActions).map(b => (co.extract(fa)._1 ++ b._1, b._2))
      }

  check((traced: TracedFree[LogAction, ProductView]) => {
    val (history, _) = traced.run(List()).run
    true
  })


  checkAll("Comonad[LogAction]", ComonadTests[LogAction].comonad[Int, Int, Int])

  implicit def comonadLogAction: Comonad[LogAction] = new Comonad[LogAction] {
    def extract[V](x: LogAction[V]) = ???
    def map[A, B](fa: LogAction[A])(f: A => B) = ???
    def coflatMap[A, B](fa: LogAction[A])(f: LogAction[A] => B) = ???
  }

  implicit def arbLogAction[V](implicit arbV: Arbitrary[V]): Arbitrary[LogAction[V]] = Arbitrary(
    arbitrary[V].map(LogAction.Pure(_))
  )

  implicit def eqLogAction[V]: Eq[LogAction[V]] = Eq.instance[LogAction[V]](
    (a, b) => ???
  )

  // implicit def arbActions[V] : Arbitrary[Free[Action,V]] = ???
  implicit def arbInt: Arbitrary[Int] = Arbitrary(Gen.posNum[Int])

  implicit def arbActions: Arbitrary[Free[Action, ProductView]] = Arbitrary(
    Gen.const(Action.viewEntity(Ref[Product](SKU("wibble"))))
  )

  implicit def arbActions2LogActions: Arbitrary[TracedFree[LogAction, ProductView]] = Arbitrary( ///*[V](implicit arbActions: Arbitrary[Free[Action, V]])*/: Arbitrary[TracedFree[LogAction, ProductView]] = Arbitrary(
                                     ???
    // arbitrary[Free[Action, V]]
    //   .map(actions => actions.foldMap(compileLogActions.andThen(trace)))
  )

  implicit def arbProductView: Arbitrary[ProductView] = Arbitrary(
    ???
  )

  implicit def run: Transaction ~> FakeStore = 
    λ[Transaction ~> FakeStore]({
      case Pure(v) => ???
      case Load(name, offset) => ???
      case Commit(name, events) => ???
    })

  val actions = for {
    product <- Free.pure(Ref.product("1234"))
    a <- product.view
    _ <- product.update(PutProductDetails("flimflam", 13.1F))
  } yield ()


  implicit def eqRawUpdate: Eq[RawUpdate] = new Eq[RawUpdate] {
    def eqv(a: RawUpdate, b: RawUpdate): Boolean = a.equals(b)
  }

  implicit def cogen[V]: Cogen[V] = Cogen(v => 13)

}
