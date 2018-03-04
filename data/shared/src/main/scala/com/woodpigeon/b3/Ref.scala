package com.woodpigeon.b3

case class Ref[E <: Entity](name: E#Key)

object Ref {
  implicit class RichRef[E <: Entity](ref: Ref[E]) {
    def send(update: Any) : Seq[Any] = Seq()
  }

  val allProducts = Ref[ProductSet]("Products")
  def product(sku: String) = Ref[Product](SKU(sku))
}





