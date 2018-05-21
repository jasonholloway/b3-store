package com.woodpigeon.b3 

import cats.{ Eval, Foldable, Traverse }
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

  trait Interpretor[C[_], S[_], M[_]] {
    
    type Step[C[_], S[_], V] = C[S[V]]
    type Interpreted[C[_], M[_], V] = C[M[V]]

    def interp[V](step: Step[C, S, V]): Interpreted[C, M, V]

    def apply[V](s: Free[S, V])(implicit C: Monad[C], M: Monad[M], CM: C ~> M, I: Monad[Interpreted[C, M, ?]]): M[V] = {
      def steppify = λ[S ~> Step[C, S, ?]](C.pure(_))
      def transform = λ[Step[C, S, ?] ~> Interpreted[C, M, ?]](interp(_))
      def flatten[V](c: Interpreted[C, M, V]) = M.flatten(CM.apply(c))
      
      flatten(s.foldMap(steppify.andThen(transform)))
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
      def interp[V](step: Step[State[String, ?], Op, V]): Interpreted[State[String, ?], Id, V] =
        step.transform {
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
      def interp[V](step: Step[Id, Op, V]): Interpreted[Id, List[?], V] = step match {
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

  "interpretation compression"-> {

    //and here we go... how can we possibly compress? Well, we need to return Left to say 'no!'
    //but in doing so we can return a updated state, so we are progressing, but we internalise our new state
    //instead of blurting it out into the wider world

    val interp = new Interpretor[Id, Op, Free[Op, ?]] {
      def interp[V](step: Step[Id, Op, V]): Interpreted[Id, Free[Op, ?], V] =  step match {
        case Append(w) => ???
      }
    }

    implicit def id2Free = λ[Id ~> Free[Op, ?]](v => Free.pure(v))

    val prog = for {
      _ <- Op.append("h")
      _ <- Op.append("e")
      o <- Op.append("y")
    } yield o

    val result = interp(prog)
    
    assert(result == "hey")
  }


  "interpreting without state" -> {

    import cats.Eval

    val interp = new Interpretor[Eval, Op, Id] {
      def interp[V](step: Step[Eval, Op, V]): Interpreted[Eval, Id, V] = step.value match {
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

  // test("blahhh") {
  //   val actions = for {
  //       product <- Free.pure(Ref.product("1234"))
  //       a <- product.view
  //       _ <- product.update(PutProductDetails("flimflam", 13.1F))
  //   } yield ()

  //   val logActions = actions.mapK(interpActions)

  //   val transactions = logActions.mapK(interpLogActions)

  //   assert(false)
  // }

  //
  //go on then, how'd you test this lot then? 
  //we want to test our Interpretor in various scenarios
  //
  //







  //there's a question of whether state should propagate between layers - I don't think it should.
  //So - this means that the whole state thing should just be a temporary thing

  //Each interpretator layer has a native context, but then the next layer has a clean slate
  //Nones should also be transparent - they must go to the very top to maintain the structure of the blancmange
  //but our closely-specified FunctionK's shouldn't be aware of them passing through.
  //each interpretor 





  //I've been lulled again by the promise of the monoid context. But the interpretation context isn't additive - it's a monad!
  //the monoid here is the endofunctor - it's what we're combining together. As such, when we begin, our Free structure of
  //naked ops is fine just with an Id monad.
  //
  //

    

  // def trace[F[_]] = λ[F ~> FreeT[F, State[History, ?], ?]](_ => ???)

  // def compAndTrace[F[_], G[_]](comp: F ~> G) =
  //   λ[F ~> FreeT[G, State[History, ?], ?]](_ => ???)


  // def traceActions[V](actions: Free[Action, V]): FreeT[Action, State[History, ?], V] =
  //   actions.foldMap(trace)

  // def traceLogActions[V](tracedActions: FreeT[Action, State[History, ?], V]): FreeT[LogAction, State[History, ?], V] = {
  //   tracedActions
  //     .mapK(λ[State[History, ?] ~> Free[LogAction, ?]](_ => ???)) //this bizarrely does very little...
  //     .foldMap(compActions)
  //     .foldMap(trace)  //but this trace is disconnected from the original...
  // }

  //but, tracing at each layer and then combining at the borders will remove the sequence of the actions 
  //WE CAN'T TRACE PER-LAYER! And that means that the state-accumulation has to be part of the compilation step each time,
  //passed along as an exterior wrapper of the ADT.

  //a StateT[Action, History, V] becomes a StateT[LogAction, History, V], accumulating its state as it goes
  //compilation then becomes a two-stage thing - the outer compilation delegates to the inner one

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

  //but as soon as we start dealing in Nones, we have to accept the possibility of Nones seeping out and up, right to the very top,
  //unless we have some arbitrary conversion from none to Free...

  //though... you know... with our transformations anyway, not only do we have the possibility of multiple instructions per base instruction,
  //there's also the likelihood of fewer high-level actions - as in, low-level actions are to be grouped together, wittled down, suppressed.
  //Reads in the business logic may be served from a cache and not be translated to outer instructions at all.

  //so translating to Frees at every step is not sufficient anyway. The idea might be for a stateful interpreter to iterate through the inner Free,
  //which would be kinda simple, but we want to only swap out our outermost layer. Cacheing etc is part of our app.

  //If we are in the business of iteratively layering our domain logic outwards - which we are! - then, in our transformations, we need to able to
  //turn multiple domain actions into only a few outer ones. For this we could firstly use an Option alongside our Free - but if we were to do so no information
  //would escape each segment, being bulwarked defensively by the Option wrapper. Instead of returning puny Option parts, which can't be reassembled as a None
  //is a complete suppression of info (except for its type), a transformation of many into one must be exactly that, a transformation that isn't natural... 

  //we need to course through our subject and build up shit
  //a natural transform is too restrictive here - via a fold such a transform can project to zero or more per section, but there is no possibility of cross-section logic
  //for this, we'd start off with the most accurate relation - the simple, honest function.
  //

  //Wrapping in Option allows us to prepend valueless state contexts
  //Free[Traced[B, ?], None], Free[Traced[B, ?], Some(12)]
  //though it feels like I'll need to extract values from their instructions if I'm doing this

  //λ[V => Free[State[History, ?], Option[V]]] 
  //but we can't do yer nice Free operations on such a lambda - the outer wrapping needs to be Free
  //nah... I think we can actually...
  //



    //   ta.runEmpty.value match {
    //     case (h, Some(a)) => a2fb(a).mapK(trace)   //need to re-inject h here...
    //     case (h, n@None) => Free.liftF(State.set(h).map(_ => n.map(a2fb))) //      T.lift(n).modify(_ => h))
    //   }
    // })

//
//
//
//
//
//

      // val q = ta.map(a => a2fb(a))

      

      // val (h, a) = ta.runEmpty.value

      // val ftb = a2fb(a).mapK(trace)

      // ftb.


      // val p = Free.liftF[λ[V => State[History, B[V]]], Unit](State.set(h).map(_ => ???))
      // val r = p.flatMap(_ => ftb)

      // p.foldMap(f: FunctionK[ => IndexedStateT[Eval, List[Int], List[Int], B[V]], M])



      //but this trick of adding a Free to the beginning relies on the Free being of the same context as that of our continuations
      //the first step of our projection is expecting an argument - but what?
      //the result of some previous, exterior step, which we ourselves receive via 'sa'

      //so, again, our starting context needs to be correctly typed, and this requires seeding nicely with an empty value
      //as long as our contextual type includes our instruction ADT, which never has a zero as we want it to be completely free,
      //we can't summon up a nobbled starting point.

      //if our context were just State[History, ?], then a State[History, Unit] would happily suffice. 
      //
  //with a single State, we can contramap, but here we don't have a single State, but a separated sequence of them
  //the very first State needs contramapping
  //




  //what do we need to do with H? Nothing? We don't need to look into it at this point
  //we only care about our current step.

  //BUT WE DO, YOU ARSE. Because the step passed in relates exactly to our own step. We never care about
  //previous steps. But state from the lower levelsof processing of our step have to be passed upwards.

  //so we do need to run, and our H does need passing upwards as part of our projected fsb.
  //We need an empty State[H, _], but not contributing to any Free structure. It needs to be hidden, tacked onto then
  //beginning untraceably.

  //Q: is there a Free instruction that skips interpretation? It'd seem pointless if that were so.
  //the hidden bits are all in the connecting tissues of the functional mappings - ie between the yieldings
  //of instructions.

  //so we need a special mapping before the very first yielding, and with it some way of pre-supplying a base of state
  //to proceed from in this particular step.

  //which will be a kind of overlay, a kind of wrapping of the more pristine Free of our primitive projeciton.
  //First, we need the pristine one, then the improved, injected one.


      //don't need to thread free through, but re-represent it
      //what does need threading through is the state only

      //we pass through no structure, as the driver of our mapping does that; all we need to do is return the structure
      //proper to our current projection. But then the composable state functions we return have to be re-composable by the outside executor
      //
      //the natural transform can't be applied eagerly, otherwise our runEmpty would make no sense - it needs to be ready populated by
      //the previous stages. When the output Free is iterated, only then will the transforms be applied - so the runEmpty above is a timely
      //unpacking of the info as it passes through. We compose our nicely typed functions to eventually perform an actual execution - this
      //here is the actual execution

      //Free[State[A]]
      // ~> State[A]
      // ~> State[Free[B]]
      // ~> (H, Free[B])          <- the problem is that we throw away the received state at this point - we NEED to keep hold of this
      // ~> (H, Free[State[B]])
      //
      //BUT! how to put the H back into the Free[State[B]], eh? BIG QUESTION.



    //   val g = ta.map(a => fab(a)).flatMap(b => {

    //     val c = b.mapK(trace)

    //     //flatMap requires a state to flatten
    //     //we can't just arbitrarily give it any old Free
    //     //we need some way of popping the Free from our mapping outside
    //     //in some kind of sequencing manouvre

    //     //we start off with a State[A], from which we project a State[Free[B]]
    //     //well... in the very start we have a Free[State[A]]
    //     //then through our translation we get a Free[Free[State[B]]]
    //     //but all this is fine

    //     //inside the gubbins of the outer translation,
    //     //we begin with a State[A], and project to a State[Free[B]]
    //     //we need to get the structure of the Free outside of the state
    //     //the structure of the wrapper should be similar-ish

    //     //for each state, we want to realise the 


        

    //     State.set(List())
    //   })

    //   // val x = ta.map(fab(_)).flatMap(_.map(b => State.set(List()).map(_ => b) )))

    //   //flatMap will only give you a State however

    //   g
    // })

  def traceActions2[V](actions: Free[Action, V]): Free[Traced[Action, ?], V] =
    actions.mapK(trace)



  // def compLogActions[V](tracedActions: FreeT[Action, State[History, ?], V]): FreeT[LogAction, State[History, ?], V] =
  //   tracedActions.(interp: (Action[FreeT[Action,  => IndexedStateT[Eval, List[Int], List[Int], β$12$], V]]) => IndexedStateT[Eval, List[Int], List[Int], FreeT[Action,  => IndexedStateT[Eval, List[Int], List[Int], β$12$], V]])
  //   tracedActions.foldMap(λ[Action ~> FreeT[LogAction, State[History, ?], ?]](_ => ???))


  //another go below... wrapping too closely in StateT requires our ADTs to implement various typeclasses - maybe we can do it with state only at the top?
  //Free keeps our ADT in cotton wool; if we expand it out, we lose its handy protection



  // def traceActions2[V](actions: Free[Action, V]): StateT[Free[Action, ?], History, V] = {
  //   StateT.lift[Free[Action, ?], History, V](actions)
  // }

  // def compTraceActions[V](actions: Free[Action, V]): StateT[Free[Action, ?], History, V] = {
  //   traceActions2(actions)
  //     .flatMap(fas: (V) => IndexedStateT[ => Free[Action, β$18$], List[Int], SC, B])
  //   actions.foldMap(λ[Action ~> StateT[Free[Action, ?], History, ?]](_ => ???))
  // }

  



  //all is traced, from the bootom upwards
  //so we'realways dealing in StateT's,rightfrom the off
  //

  //
  //

    // actions
    //   .foldMap(compActions.andThen(trace).andThen(resequence))


  //a trace could just take a Free as input, as standard


//
//
//




  //but the flatten takes the laziness out of it, or rather ghettoizes it
  //the question is whether this flattening killssome of the laziness

  //the compilation has to return a composite Free, as we must insert ligatures
  //kk
  //



  //tis fine: just need to extract the State, flatten the actions, and rewrap in a combined state
  //and you extract the state by... 
  //how do you get the state out of it then? 


  //there's a question of whether our natural translation will be into a Free - why would it ever translate to a Free?
  //kjj

  //monads - are they lazy? Who knows. They're functions, of course.
  //so when you're putting em together, you're not necessarily
  //designating the exact invocation of em

  //but how are they strung together? There's no iteration involved, necessarily.
  //there'sa single lzy function, resolving to a single value. How can a list then be a monad?
  //it must be a recursive monad, repeatedly yielding yet keeping something back.
  //it yields by returning its own shape, but always with an unwrapped value at its foot.
  //there's then little list-like about the above,despite it being monadic.

  //but there is, dumbo! A free is list-like, as it is recursively unwrapped.
  //so if you're flatmapping Frees, then you're composing layer within layer of functions,
  //and this composition goes through the layers as if they weren't there

  //or does it? The Free must somehow iterate through itself.
  //and indeed it does, though i don't follow how it does so.

  //so a flatmap will... not eagerly evaluate, but allow itself to be recombined structurally
  //calling .flatMap is actually like quoting - it calcifies the operation as a value, to be interpreted at leisure
  //and then only at the last possible moment is the structure stepped through.

  //but still, even though it is lazy, the interpretation still follows a certain fixed path.
  //but this path piles up layers of interpreters before actually reaching the original tree-like iterative structure
  //at the bottom of the pot, is the pristine shape, all else is mapping, and each layer of mapping applies to each step of the original structure.

  //and so.. a flatmap is absolutely fucking perfectly fine, thank you very much.

  //but just hoping for monads gives you series...
  //which is alright, except the series has to come *after* the state extraction

  //given our traced actions
  //the traces should be unwrapped from state
  //and further translated
  //while the states of both layers are combined
  //it's all kinda similar to monads' flatmaps
  //
  //in fact, it's exactly the same: unwrap, map, recombine
  //

  // val traceLogActions = traceActions.andThen(λ[Traced[Action, ?] ~> Traced[LogAction, ?]](s => s.map .mapK(compileLogActions)

  // val traceTransaction = λ[Action ~> Traced[Transaction, ?]](_ => ???)


  check((compilation: CompilationHistory[LogAction]) => {
    // ???
    true
  })


  //go on then, why do we need a magical way to extract from an Action? This is needless.
  //Our monad-wrangling should just involve Frees, surely - the Actions are effectively values
  //passed about by the monadic mechanism

  //the tracing needs to happen much sooner - our initial Free[Action, V] should be mapped to be Free[StateT[Action, History, V]] 
  //that is - the State should be on the inside, with the Free mechanism still doing its gymnastics on the outside

  //and so...
  //a Free[StateT[Action, History, ?], V]
  //from a Free[Action, V]

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
    })


  implicit def eqRawUpdate: Eq[RawUpdate] = new Eq[RawUpdate] {
    def eqv(a: RawUpdate, b: RawUpdate): Boolean = a.equals(b)
  }

  implicit def cogen[V]: Cogen[V] = Cogen(v => 13)

}

