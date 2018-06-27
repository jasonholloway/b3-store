package com.woodpigeon.b3 

import cats.functor.Bifunctor
import cats.{ Distributive, Eval, Foldable, Traverse }
import cats.arrow.FunctionK
import cats.data.{ IdT, StateT }
import cats.free.FreeT
import cats.{ Applicative, InvariantMonoidal, Monad }
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
import com.woodpigeon.b3.Action.ViewEntity
import com.woodpigeon.b3.Transaction._
import com.woodpigeon.b3.schema.v100.{ProductView, PutProductDetails}
import org.scalacheck._
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.Checkers
import org.typelevel.discipline.scalatest.Discipline
import cats.instances.tuple._
import cats.instances.int._
import scala.Some
import scala.annotation.tailrec

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
  case class CompilationHistory[Target[_]](history: History)


  type History = List[Int]

  object History {
    def apply(v: Int*) = List[Int](v:_*)
  }




  import cats.Id
  import cats.Monoid
  import cats.Bifunctor


  object Implicits {


    implicit def monadInAMonadMonad2[C[_], M[_]](implicit C: Monad[C] with Distributive[C] with Comonad[C], M: Monad[M], E: Bifunctor[Either[?, ?]],  M2C: M ~> C): Monad[λ[v => C[M[v]]]] =
      new Monad[λ[v => C[M[v]]]] {
        def pure[A](x: A): C[M[A]] = C.pure(M.pure(x))

        def flatMap[A, B](cma: C[M[A]])(f: A => C[M[B]]): C[M[B]] =
          C.flatMap(cma)(ma => {
            val cmmb = C.distribute(ma)(f(_))
            val cmb = C.map(cmmb)(M.flatten(_))
            cmb
          })

        // def tailRecM2[A, B](a: A)(f: A => C[M[Either[A, B]]]) = {

        //   val cme = C.tailRecM(M.pure(Left(a): Either[A, B]))(me => {
        //     val ce = M2C(me)
        //     C.map(ce)(e => {

        //       //HERE! we need switch on the e

        //       E.bimap(e)(
        //         a => M.map(me)(_ => Left(a): Either[A, B]),
        //         b => M.map(me)(_ => Right(b): Either[A, B])
        //       )
        //     })
        //   })

        //   C.map(cme)(me => ???)
          
        //   ???
        // }


        def tailRecM2[A, B](a: A)(f: A => C[M[Either[A, B]]]) = {

          val cme = C.tailRecM(M.pure(Left(a): Either[A, B]))(me => {

            //HOW TO GET 'a' HERE?
            //we have to peek into M as well? 

            val cme = f(a) //here we have the fullfat value

            val ce = C.flatMap(cme)(M2C(_))  //wastey but useful

            val me: M[Either[A, B]] = ???

            val ceme = C.map(ce)(E.bimap(_)(
              a => M.map(me)(_ => Left(a): Either[A, B]),
              b => M.map(me)(_ => Right(b): Either[A, B])
            ))

            ceme
          })

          C.map(cme)(me => ???)
          
          ???
        }


        //now we've got cme: C[M[Either[A, B]]]
        //which has all the f's folded in, despite being the type of a single f output
        //we need to reduce it to C[M[B]]
        //
        //BUT! we haven't actually got an f() in our entire above concoction!
        //so nothing is folded in - instead we've effectively done a pure() on a
        //good progress nevertheless
        //
        //
        //
        //


        //right... so we have to write our own bespoke implementation.
        //essentially, we need to be able to sink one of the monads into the other, without actually losing that monad
        //going C~>M gives us enough to carry on going, but we still have the original C hanging about
        //the problem with relying on M.tailRecM or whatever is that we're severely constrained as to what we can actually pass through
        //the cascading evaluations
        //we can get to M[Either[A,B]] => C[Either[M[Either[A,B]], M[Either[A,B]]]], if we allow a sneaky peeking at the results of M
        //this lets us bring the either out of M and into C
        //at each loop of the recursion, we need to then access the result of the M passed along to us 
        //we can do this by absorbing into the dominant context of the tail recursion 
        //
        //but don't we there by lose the inferior context? Nope! - we only convert it temporarily
        //the full-blown context is passed along as a value 
        //
        //
        //



        //if i can get the one below to recurse without blowing the stack, we can expand it easily to break via Either
        def tailRecM[A, B](a: A)(f: A => C[M[Either[A, B]]]) = {

          val cme = f(a)




          C.tailRecM(a)(aa => {
            val cme = f(aa)
            ???
          })


          C.tailRecM(M.pure(a))(ma => {
            val cmme = C.distribute(ma)(f(_))
            val cme = C.map(cmme)(M.flatten(_))

            val me = C.extract(cme)
            //so, the above allows us to peek inside, but if it's like a task or something, then we can't block till the task completes, can we?
            //that'd be a dirty hack, really we're asking to be able to convert the context into Identity, which would effectively be an immediate, blocking evaluation.
            //Id could be aborbed into the current computation no questions asked. Though, really, we'd want to be able to apecify the exact type to model the
            //computation. But then tailRecM would be blown apart again, as tailRecM is a special facility of the current mode of computation, that requires immediate access sto their
            //fruits of its labour. You can't just switch into another mode when you wish and expect the mechanism to function as if this new layer of ambiguity wasn't there.

            //C[M[?]] is such a combined context - so we can't just use one and hope to hide the other away (well, we can, but this requires us to expect absorption, like
            //with Id, or C~>M). We don't want absorption, as that destroys visible structure, which our interpretor needs. So we have to keep both the modesof computation about.

            //and so we can't just delegate to the tailRecM of one of them only - we need to write our own, relying on other facilities of the individual monads.
            //


            //I'd need to be able to 'distribute' M as well...
            //it seems... 
            //the either needs to get to the top...
            //I mean, we want to keep the M around, but it'sbe nice to peek into it - which is a kind of special power. A non-destructive peeking.
            //Peeking into a task implies evaluation - in fact all peeking needs evaluation - but this should be transparent
            //
            //But evaluating doesn't mean annihilating the context. If we can peek into the innards, then we can repackage the Either to be consumable by tailRecM
            //
            //


            ???
          })
        }

      }
  }




    implicit def monadInAMonadMonad[C[_], M[_]](implicit C: Monad[C], M: Monad[M], CM: C ~> M): Monad[λ[v => C[M[v]]]] =
      new Monad[λ[v => C[M[v]]]] {
        def pure[A](x: A): C[M[A]] = C.pure(M.pure(x))

        def flatMap[A, B](cma: C[M[A]])(f: A => C[M[B]]): C[M[B]] =
          C.map(cma) {
            M.flatMap(_) { a =>
              M.flatten(CM(f(a)))
            }
          }


        //if i can get the one below to recurse without blowing the stack, we can expand it easily to break via Either
        def tailRecM2[A, B](a: A)(f: A => C[M[A]]) = {
          C.flatMap(C.pure(M.pure(a))) {
            ma => M.flatMap(ma)(f(_))
          }
        }

        def tailRecM3[A, B](a: A)(f: A => C[M[Either[A, B]]]): C[M[B]] = {
          C.tailRecM(M.pure(a)) {
            ma => {
              val n = M.map(ma)(f(_))

          n
              //this is where I need a distributive law in place

              n
            }
          }
        }



        def tailRecM[A, B](a: A)(f: A => C[M[Either[A, B]]]): C[M[B]] = {
          println("tailRecM alert!", a)
          val mb = M.tailRecM(a)(aa => M.flatten(CM(f(aa))))
          C.pure(mb)

          //so... the problem here is that we're losing the composable C
          //it can't really be submerged into M so easily...
          //it sinks without a trace. Even if it were semantically contained within the result,
          //we'd have no way of dredging it out again for composing together?
        }
      }
  }

  trait Interpretor[C[_], S[_], M[_]] {
    
    type From[V] = C[S[V]]
    type To[V] = C[M[V]]

    def interp[V](from: From[V]): To[V]

    def apply[V](s: Free[S, V])(implicit C: Monad[C], M: Monad[M], CM: C ~> M, I: Monad[To[?]]): M[V] = {
      def prep = λ[S ~> From[?]](C.pure(_))
      def transform = λ[From[?] ~> To[?]](interp(_))
      def finish[V](c: To[V]) = M.flatten(CM.apply(c))
      
      finish(s.foldMap(prep.andThen(transform)))
    }

  }

  
  import Behaviours._

  sealed trait Op[V]
  case class Set(w: String) extends Op[String]
  case class Append(w: String) extends Op[String]

  object Op {
    def set(w: String): Free[Op, String] = Free.liftF[Op, String](Set(w))
    def append(w: String): Free[Op, String] = Free.liftF[Op, String](Append(w))
  }
  
  "interpreting with state" -> {

    val interp = new Interpretor[State[String, ?], Op, Id] {
      def interp[V](from: From[V]): To[V] =
        from.transform {
          case (s, Set(w)) =>
            (w, w)
          case (s, Append(w)) =>
            val s2 = s + w
            println((s, w, s2))
            (s2, s2)
        }
    }

    implicit def stateExtractor[S](implicit S: Monoid[S]): State[S, ?] ~> Id =
      λ[State[S, ?] ~> Id](_.runEmptyA.value)


    test("accumulates state") {
      val prog = for {
        _ <- Op.set("boo!")
        o <- Op.append(" rah!")
      } yield o

      val result = interp(prog)

      assert(result == "boo! rah!")
    }

  }
  
  "interpretation fan-out" -> {

    import cats.instances.list._

    val interp = new Interpretor[Id, Op, List[?]] {
      def interp[V](from: From[V]): To[V] = from match {
        case Set(v) => List(v)
        case Append(v) => List(v, v, v)
      }
    }

    implicit def id2List = λ[Id ~> List](v => List(v))

    test("yields many") {
      val prog = for {
        _ <- Op.set("schnurp")
        o <- Op.append("wibblewibble")
      } yield o

      val result = interp(prog)

      assert(result == List("wibblewibble", "wibblewibble", "wibblewibble"))
    }
  }

  import cats.Eval
  import cats.instances.string._
  import Implicits._

  implicit def state2Free = λ[State[String, ?] ~> Free[Op, ?]](s => Free.pure(s.runEmptyA.value))
  implicit def id2Free = λ[Id ~> Free[Op, ?]](v => Free.pure(v))
  implicit def id2Eval = λ[Id ~> Eval](Eval.now(_))

  "interpretation compression"-> {

    val interp1 = new Interpretor[State[String, ?], Op, Free[Op, ?]] {
      def interp[V](from: From[V]): To[V] = {
        from.transform((s: String, o: Op[V]) => {
          println((s, o))    //s is ALWAYS empty! None of the preceding state is being passed in
          o match {
            case Append("!") => ("", Op.append(s))
            case Append(w) => (s + w, Free.pure(w))
          }
        })
      }
    }

    val interp2 = new Interpretor[Id, Op, Eval] {
      def interp[V](from: From[V]): To[V] = from match {
        case Append(w) => Eval.now(w)
        case _ => ???
      }
    }

    val prog = for {
      _ <- Op.append("h")
      _ <- Op.append("e")
      _ <- Op.append("y")
      o <- Op.append("!")
    } yield o


    val intermediate = interp1(prog)
    val result = interp2(intermediate).value
    
    assert(result == "hey!")
  }


  "interpreting without state" -> {

    import cats.Eval

    val interp = new Interpretor[Eval, Op, Id] {
      def interp[V](from: From[V]): To[V] = from.value match {
        case Set(v) => Eval.now(v)
        case Append(w) => Eval.now(w)
      }
    }

    implicit def eval2Id: Eval ~> Id = λ[Eval ~> Id](e => e.value)
    implicit def id2Id[A[_]]: A ~> A = λ[A ~> A](a => a)

    test("interprets value") {
      val prog = for {
        _ <- Op.set("ya")
        o <- Op.set("boo!")
      } yield o

      val result = interp(prog)

      assert(result == "boo!")
    }

    test("interpretation involving further suspension") {
      //this currently makes no sense
      val prog = for {
        _ <- Op.append("IGNORED")
        s <- Op.append("HELLO!")
      } yield s

      val result = interp(prog)

      assert(result == "HELLO!")
    }

  }


  import cats.instances.int._
  import cats.instances.list._
  import cats.instances.option._
  import cats.FlatMap
  import cats.Monoid

  type Traced[A[_], V] = State[History, A[V]]


  def trace[A[_]]: A ~> Traced[A, ?] = ???


  import cats.syntax.option._

  def traced[A[_], B[_]](a2fb: A ~> Free[B, ?]) =
    λ[Traced[A, ?] ~> Free[Traced[B, ?], ?]](ta => {
      ta.runEmpty.value match {
        case (h, a) => {
          project(h, a, a2fb)
          ???
        }
      }
    })


  def project[A[_], B[_], V](h: History, a: A[V], a2fb: A ~> Free[B, ?]) : Free[Traced[B, ?], Option[V]] = {
    a2fb(a).mapK(trace)
      .mapK(λ[λ[V => State[History, B[V]]] ~> λ[V => State[History, Option[B[V]]]]](tb => {

        tb.map(Some(_))

      }))

    ???


  }


  def traceActions2[V](actions: Free[Action, V]): Free[Traced[Action, ?], V] =
    actions.mapK(trace)



  check((compilation: CompilationHistory[LogAction]) => {
    // ???
    true
  })


  import LogAction._

  implicit def arbCompilation[Target[_]]: Arbitrary[CompilationHistory[Target]] = Arbitrary(
    CompilationHistory[Target](List())
  )

  implicit def arbLogAction[V](implicit arbV: Arbitrary[V]): Arbitrary[LogAction[V]] = Arbitrary(
    arbitrary[V].map(v => LogAction.Pure(v))
  )

  implicit def eqLogAction[V]: Eq[LogAction[V]] = Eq.instance[LogAction[V]](
    (a, b) => ???
  )

  // implicit def arbActions[V] : Arbitrary[Free[Action,V]] = ???
  implicit def arbInt: Arbitrary[Int] = Arbitrary(Gen.posNum[Int])

  implicit def arbActions: Arbitrary[Free[Action, ProductView]] = Arbitrary(
    Gen.const(Action.viewEntity(Ref[Product](SKU("wibble"))))
  )

  implicit def arbActions2LogActions: Arbitrary[Traced[LogAction, ProductView]] = Arbitrary( ///*[V](implicit arbActions: Arbitrary[Free[Action, V]])*/: Arbitrary[TracedFree[LogAction, ProductView]] = Arbitrary(
                                     ???
    // arbitrary[Free[Action, V]]
    //   .map(actions => actions.foldMap(compileLogActions.andThen(trace)))
  )

  implicit def arbProductView: Arbitrary[ProductView] = Arbitrary(
    ???
  )

  implicit def run: Transaction ~> FakeStore = 
    λ[Transaction ~> FakeStore]({
      // case Pure(v) => ???
      case Load(name, offset) => ???
      case Commit(name, events) => ???
      case _ => ???
    })


  implicit def eqRawUpdate: Eq[RawUpdate] = new Eq[RawUpdate] {
    def eqv(a: RawUpdate, b: RawUpdate): Boolean = a.equals(b)
  }

  implicit def cogen[V]: Cogen[V] = Cogen(v => 13)

}

