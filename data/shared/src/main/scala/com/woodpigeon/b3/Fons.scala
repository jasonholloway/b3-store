package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import Stringifiers._
import Handlers._


case class BadAggregationTypeException() extends Exception


sealed trait Entity {
  type Name
  type View
}

sealed trait Product extends Entity { type Name = ProductSpec; type View = ProductView }

sealed trait ProductSet extends Entity { type Name = String; type View = ProductSetView }



case class Ref[E <: Entity](name: E#Name)

object Ref {
  implicit class RichRef[E <: Entity](ref: Ref[E]) {
    def send(update: Any) : Seq[Any] = Seq()
  }

  val allProducts = Ref[ProductSet]("Products")
  def product(sku: String) = Ref[Product](ProductSpec(sku))
}



class Fons(log: EventLog) {

  def view[E <: Entity](ref: Ref[E])(implicit stringifier: Stringifier[E#Name], handler: Handler[E]) : Future[E#View] =
    loadEvents(stringifier.stringify(ref.name))
      .map { aggregate(ref.name, _) }


  private def aggregate[E <: Entity](name: E#Name, events : Seq[Event])(implicit stringifier: Stringifier[E#Name], handler: Handler[E]) : E#View =
    events.map { _.inner.value }
      .foldLeft(handler.create(name)) {
        (ac, u) => handler.update(ac, u)
                    .getOrElse(ac)
      }

  private def loadEvents(ref: String) : Future[Seq[Event]] = {
    val offsetMap = StreamOffsetMap(offsets = Map(ref -> 0))
    log.read(offsetMap)
      .map(_.fragments.find(f => f.ref == ref).map(_.events).getOrElse(Seq()))
  }

}




trait Stringifier[Val] {
  def stringify(v: Val): String
}

trait Handler[E <: Entity] {
  def create(name: E#Name): E#View
  def update(ac: E#View, update: Any): Option[E#View]
  def project(name: E#Name, ac: E#View, version: Int, update: Any): Seq[Any]
}


case class ProductSpec(sku: String)







case class LogUpdate[E <: Entity](ref: Ref[E], update: Update)






object Handlers {

  implicit def productHandler(implicit stringifier: Stringifier[ProductSpec])
    = new Handler[Product] {
      def create(name: ProductSpec): ProductView
        = ProductView().withSku(stringifier.stringify(name))

      def update(ac: ProductView, update: Any): Option[ProductView] = update match {
        case PutProductDetails(name, price) => Some(ac.withName(name)
                                                      .withPrice(price))
        case _ => None
      }

      def project(name: ProductSpec, ac: ProductView, version: Int, update: Any): Seq[Any] =
        if(version == 1) Ref.allProducts.send(AnnounceProduct(name.sku)) else Nil

    }


  implicit val productSetHandler = new Handler[ProductSet] {
    def create(name: String): ProductSetView = ProductSetView()

    def update(ac: ProductSetView, update: Any): Option[ProductSetView] = update match {
        case AnnounceProduct(sku) => Some(ac.addSkus(sku))
        case _ => None
      }

    def project(name: String, ac: ProductSetView, version: Int, update: Any): Seq[Any] = Seq()
  }


}



object Stringifiers {
  implicit val stringifyString: Stringifier[String] =
    s => s

  implicit val stringifyProductSpec: Stringifier[ProductSpec] =
    spec => spec.sku
}


//object Projectors {
//
//  implicit val projectProduct : Projector[ProductSpec, ProductInfo] =
//    (x: UpdateContext[ProductSpec, ProductInfo]) => {
//      if(x.version == 1) EntityRef.allProducts.send(AnnounceProduct(x.key.sku))
//      else Nil
//    }
//
//}


