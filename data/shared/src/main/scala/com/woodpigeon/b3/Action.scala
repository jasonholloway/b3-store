package com.woodpigeon.b3

import cats.free._

sealed trait Action[+V]

object Action {
  case class ViewEntity[E <: Entity](ref: Ref[E], behaviour: Behaviour[E]) extends Action[E#View]
  case class UpdateEntity[E <: Entity](ref: Ref[E], update: RawUpdate) extends Action[E#View]
  
  def viewEntity[E <: Entity](ref: Ref[E])(implicit behaviour: Behaviour[E]): Free[Action, E#View] = Free.liftF[Action, E#View](ViewEntity(ref, behaviour))
  def updateEntity[E <: Entity](ref: Ref[E], update: RawUpdate): Free[Action, E#View] = Free.liftF[Action, E#View](UpdateEntity(ref, update))
}





// sealed trait Op[V]
// case class Whisper(s: String) extends Op[String]
// case class Scream(s: String) extends Op[String]


// object blah {

//   def whisper(s: String) = JFree.lift(Whisper(s))
//   def scream(s: String) = JFree.lift(Scream(s))

//   val program = for {
//     greeting <- whisper("yo!")
//     exclaim <- scream("13")
//     whimper <- whisper(exclaim)
//   } yield greeting + "!"

// }



// import cats.Id
// import cats.Monad
// import cats.~>

// sealed trait JFree[S[_], A] {
//   import JFree._

//   def map[B](ab: A => B): JFree[S, B] = flatMap(a => Pure(ab(a)))
//   def flatMap[B](afb: A => JFree[S, B]): JFree[S, B] = FlatMap(this, afb)


//   def interpret[T[_]](st: S ~> T)(implicit T: Monad[T]): T[A] = this match {
//     case Pure(v) => T.pure(v)
//     case Suspend(s) => st(s)
//     case FlatMap(inner, project) =>
//       T.flatMap(inner.interpret(st)) {
//         project.andThen(_.interpret(st))
//       }
//   }



//   //
//   //

// }

// object JFree {
//   case class Pure[F[_], V](v: V) extends JFree[F, V]
//   case class Suspend[F[_], V](f: F[V]) extends JFree[F, V]
//   case class FlatMap[S[_], A, B](fsa: JFree[S, A], asb: A => JFree[S, B]) extends JFree[S, B]
  
//   def lift[F[_],V](f: F[V]) = Suspend(f)

// }


