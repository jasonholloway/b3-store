package com.woodpigeon.b3

import com.woodpigeon.b3.Behaviours._
import com.woodpigeon.b3.RawUpdate._
import com.woodpigeon.b3.schema.v100._
import org.scalatest.AsyncFreeSpec

import scala.concurrent.Future
import scala.language.implicitConversions




class FonsTests extends AsyncFreeSpec {

  val logSource = new LogSource {
    def read(logName: String, offset: Int): Future[LogSpan] = ???
  }


  "Products" - {

    "SKU determined by name" - {
      new Fons(logSource)
        .view(Ref.product("GLOVE3"))
        .map(v => assert(v.sku == "GLOVE3"))
    }

    "given some PutProductDetails" - {
      val log = new InMemoryEventLog()
      log.stream("Product#FLUFFY1").append(
        PutProductDetails("Fluffy socks")
      )

      "should return those details" in {
        new Fons(logSource).view(Ref.product("FLUFFY1"))
            .map(v => {
              assert(v.sku == "FLUFFY1")
              assert(v.name == "Fluffy socks")
            })
      }
    }
  }


  "ProductSet" - {
    "given some products" - {
      val log = new InMemoryEventLog()
      log.stream("Product/SOCKS1").append(PutProductDetails("Fluffy socks"))
      log.stream("Product/SOCKS2").append(PutProductDetails("Coarse bristly mittens"))

      "should have two SKUs in it" in {
        val fons = new Fons(logSource)
        fons.view(Ref.allProducts)
            .map(view => {
              assert(view.skus.lengthCompare(2) == 0)
            })
      }

    }
  }


  "ProductSet collects all created products" in {
    val log = new InMemoryEventLog()
    log.stream("Product/p123").append(
      new PutProductDetails("Velvet Socks", 0.50f)
    )

    log.stream("Product/p456").append(
      new PutProductDetails("Hair Shirt", 13.33f)
    )

    val fons = new Fons(logSource)
    fons.view(Ref.allProducts)
        .map(productSet => {
          assert(productSet.skus == Seq("p123", "p456"))
        })
  }

}
