package com.woodpigeon.b3


sealed trait Named[E <: Entity]

case class Ref[E <: Entity](name: E#Key) extends Named[E]

case class Loaded[E <: Entity](name: E#Key, view: E#View) extends Named[E] 

object Ref {
  val allProducts = Ref[ProductSet]("Products")
  def product(sku: String) = Ref[Product](SKU(sku))
}

object Named {
  def unapply[E <: Entity](named: Named[E]): Option[E#Key] = named match {
    case Ref(name) => Some(name)
    case Loaded(name, _) => Some(name)
    case _ => None
  }
}

