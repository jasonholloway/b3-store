import com.woodpigeon.b3._
import org.scalatest.AsyncFreeSpec
import com.woodpigeon.b3.Update._
import Handlers._
import Stringifiers._
import com.woodpigeon.b3.schema.v100._
import scala.language.implicitConversions

class AggregationTest extends AsyncFreeSpec {

  "Products" - {
    "given some PutProductDetails"- {
      val log = new InMemoryEventLog()
      log.stream("Product/FLUFFY1").append(
        PutProductDetails("Fluffy socks")
      )

      "should return those details" in {
        new Fons(log).view(Ref.product("FLUFFY1"))
            .map(view => {
              assert(view.sku == "FLUFFY1")
              assert(view.name == "Fluffy socks")
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
        val fons = new Fons(log)
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

    val fons = new Fons(log)
    fons.view(Ref.allProducts)
        .map(productSet => {
          assert(productSet.skus == Seq("p123", "p456"))
        })
  }

}
