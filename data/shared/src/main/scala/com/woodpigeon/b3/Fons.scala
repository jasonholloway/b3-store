package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import Namers._
import Creators._
import Updaters._

case class BadAggregationTypeException() extends Exception



abstract class Ref[K :Namer, V :Creator :Updater] {
  type Key = K
  type View = V
  val key: Key
}

case class ProductRef(key: ProductSpec) extends Ref[ProductSpec, ProductInfo]

case class ProductSetRef(key: String) extends Ref[String, ProductSet]




object Ref {
  implicit class RichRef[K, V](ref: Ref[K, V]) {
    def send(update: Any) : Seq[Any] = Seq()
  }

  val allProducts = ProductSetRef("Products")
  def product(sku: String) = ProductRef(ProductSpec(sku))
}






class Fons(log: EventLog) {

  def view[K :Namer, V :Creator :Updater](ref: Ref[K, V])(implicit namer: Namer[K]) : Future[V] =
    loadEvents(namer.name(ref.key)) map { aggregate[V] }



  private def aggregate[V](events : Seq[Event])(implicit creator: Creator[V], updater: Updater[V]) : V =
    events.map { _.inner.value }
      .foldLeft(creator.create()) {
        (ac, u) => updater.update(ac, u)
                    .getOrElse(ac)
      }



  private def loadEvents(ref: String) = {
    val offsetMap = StreamOffsetMap(offsets = Map(ref -> 0))
    log.read(offsetMap)
      .map(_.fragments.head.events)
  }

}


trait Creator[T] {
  def create() : T
}


object Creators {

  implicit val newProductSet : Creator[ProductSet] =
    () => ProductSet()

  implicit val newProductInfo : Creator[ProductInfo] =
    () => ProductInfo()

}


//updaters are to also return follow ons
//so - a more complicated return type?
//maybe
//



trait Updater[V] {
  def update(ac: V, update: Any): Option[V]
}



trait Namer[N] {
  def name(key: N): String
}

object Namers {

  implicit val nameString: Namer[String] =
    s => s

  implicit val nameProductSpec: Namer[ProductSpec] =
    spec => spec.sku

}





case class ProductSpec(sku: String)




case class UpdateContext[K :Namer, V](view: V, key: K, version: Int, update: Any)



trait Projector[K, V] {
  def project(x: UpdateContext[K, V]) : Seq[Any]
}

object Projectors {

  implicit val projectProduct : Projector[ProductSpec, ProductInfo] =
    (x: UpdateContext[ProductSpec, ProductInfo]) => {
      if(x.version == 1) Logs.products.send(AnnounceProduct(x.key.sku))
      else Nil
    }

}





case class LogUpdate[K, V](ref: Ref[K, V], update: Update)


//
//case class LogRef[K, V](key: K)



object Updaters {

  implicit val updateProductInfo : Updater[ProductInfo] =
    (productInfo: ProductInfo, update: Any) => update match {
      case PutProductDetails(sku, name, price) => {
        Some(productInfo
              .withSku(sku)
              .withName(name)
              .withPrice(price))
      }
      case _ => None
    }


  implicit val updateProductSet : Updater[ProductSet] =
    (productSet: ProductSet, update: Any) => update match {
      case PutProductDetails(sku,name, price) => Some(productSet)
      case _ => None
    }

}


object Fons {

}


