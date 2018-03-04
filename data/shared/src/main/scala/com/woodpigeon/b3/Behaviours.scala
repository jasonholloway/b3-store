package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.{AnnounceProduct, ProductSetView, ProductView, PutProductDetails}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Behaviours {


  implicit val productSetBehaviour = new Behaviour[ProductSet] {

    def name(key: String): String = "Products"

    def create(name: String): ProductSetView = ProductSetView()

    def update(curr: ProductSetView, update: Any): Option[ProductSetView] = update match {
      case AnnounceProduct(sku) => Some(curr.addSkus(sku))
      case _ => None
    }

    def project(x: Updater, key: String, before: ProductSetView, after: ProductSetView, update: Any): Future[_] =
      Future()

  }


  implicit val productBehaviour = new Behaviour[Product] {

    def name(sku: SKU): String = s"Product#${sku.string}"

    def create(sku: SKU): ProductView =
      ProductView().withSku(sku.string)

    def update(curr: ProductView, update: Any): Option[ProductView] = {
      update match {
        case PutProductDetails(name, price) => 
          Some(curr.withName(name).withPrice(price))
        case _ => None
      }
    }

    def project(x: Updater, sku: SKU, before: ProductView, after: ProductView, update: Any): Future[_] =
      x.write(Ref.allProducts, AnnounceProduct(sku.string))

  }



}
