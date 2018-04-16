package com.woodpigeon.b3

import Action._
import cats.free.Free

sealed trait Named[E <: Entity]

case class Ref[E <: Entity](name: E#Key) extends Named[E]

case class Loaded[E <: Entity](name: E#Key, view: E#View) extends Named[E] 

object Ref {
  val allProducts: Ref[ProductSet] = Ref[ProductSet]("Products")
  def product(sku: String) = Ref[Product](SKU(sku))

  implicit class RichRef[E <: Entity : Behaviour](ref: Ref[E]) {
    def view: Free[Action, E#View] = viewEntity(ref)
    def update(ev: RawUpdate): Free[Action, E#View] = updateEntity(ref, ev)
  }
}

object Named {
  def unapply[E <: Entity](named: Named[E]): Option[E#Key] = named match {
    case Ref(name) => Some(name)
    case Loaded(name, _) => Some(name)
    case _ => None
  }
}

