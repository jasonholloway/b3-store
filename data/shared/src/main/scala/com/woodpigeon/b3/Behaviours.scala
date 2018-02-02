package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.{AnnounceProduct, ProductSetView, ProductView, PutProductDetails}

object Behaviours {

  implicit val productBehaviour = new Behaviour[Product] {

      def name(sku: SKU): String = s"Product#${sku.string}"

      def create(sku: SKU): ProductView
        = ProductView().withSku(sku.string)

      def update(ac: ProductView, update: Any): Option[ProductView] = update match {
        case PutProductDetails(name, price) => Some(ac.withName(name)
                                                      .withPrice(price))
        case _ => None
      }

      def project(sku: SKU, ac: ProductView, version: Int, update: Any): Seq[Any] =
        if(version == 1) Ref.allProducts.send(AnnounceProduct(sku.string)) else Nil

    }


  implicit val productSetBehaviour = new Behaviour[ProductSet] {

    def name(key: String): String = "Products"

    def create(name: String): ProductSetView = ProductSetView()

    def update(ac: ProductSetView, update: Any): Option[ProductSetView] = update match {
        case AnnounceProduct(sku) => Some(ac.addSkus(sku))
        case _ => None
      }

    def project(name: String, ac: ProductSetView, version: Int, update: Any): Seq[Any] = Seq()
  }


}
