import com.woodpigeon.b3._
import org.scalatest.AsyncFreeSpec
import com.woodpigeon.b3.Update._
import Updaters._
import Creators._
import Namers._
import Projectors._
import com.woodpigeon.b3.schema.v100._

import scala.language.implicitConversions

class AggregationTest extends AsyncFreeSpec {


  "Products" - {

    "given some PutProductDetails"- {

      val log = new InMemoryEventLog()
      log.stream("Product/1234").append(
        PutProductDetails("SOCKS1", "Fluffy socks")
      )

      "should return those details" in {
        new Fons(log).view(Ref.product("1234"))
            .map(view => {
              assert(view.sku == "SOCKS1")
              assert(view.name == "Fluffy socks")
            })
      }
    }
  }


  "ProductSet 'Products'" - {

    "given some products" - {

      val log = new InMemoryEventLog()
      log.stream("Product/1234").append(PutProductDetails("SOCKS1", "Fluffy socks"))
      log.stream("Product/2234").append(PutProductDetails("SOCKS2", "Coarse bristly mittens"))

      "should have two SKUs in it" in {
        val fons = new Fons(log)
        fons.view(Ref.allProducts)
            .map(view => {
              assert(view.skus.lengthCompare(2) == 0)
            })
      }

      //...

    }

  }


  "ProductSet collects all created products" in {
    val log = new InMemoryEventLog()
    log.stream("666").append(
      new PutProductDetails("p123", "Velvet Socks", 0.50f)
    )

    log.stream("667").append(
      new PutProductDetails("p456", "Hair Shirt", 13.33f)
    )

    val fons = new Fons(log)
    fons.view(Ref.allProducts)
        .map(productSet => {
          assert(productSet.skus == Seq("p123", "p456"))
        })
  }


//
//  "ProductInfo" - {
//    val log = new InMemoryEventLog()
//
//    log.stream("1234").append()
//
//    val fons = new Fons(log)
//    val info = fons.viewAs[ProductInfo]("1234", "ProductInfo")
//
//    ???
//  }

}
