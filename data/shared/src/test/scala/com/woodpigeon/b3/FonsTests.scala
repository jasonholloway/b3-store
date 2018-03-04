package com.woodpigeon.b3

import com.woodpigeon.b3.Behaviours._
import com.woodpigeon.b3.RawUpdate._
import com.woodpigeon.b3.schema.v100._
import org.scalatest.AsyncFreeSpec
import scala.concurrent.Future

class FonsTests extends AsyncFreeSpec {

  "Products" - {

    "given no events" - {

      "SKU determined by name" - {
        
        val fons = new Fons({
          case "Product#GLOVE3" => Future(EventSpan())
        })

        fons
          .view(Ref.product("GLOVE3"))
          .map(v => assert(v.sku == "GLOVE3"))
      }
    }


    "given some PutProductDetails" - {

      val fons = new Fons({
        case "Product#FLUFFY1" => Future(EventSpan(PutProductDetails("Fluffy socks")))
      })

      "should return those details" in {
         fons
           .view(Ref.product("FLUFFY1"))
           .map(v => {
               assert(v.sku == "FLUFFY1")
               assert(v.name == "Fluffy socks")
           })
       }
     }
  }


  ///////////////////////////////////////////////////////////////////////////////////////////////////
  // "ProductSet" - {                                                                              //
  //   "given some products" - {                                                                   //
  //                                                                                               //
  //     val fons = new Fons({                                                                     //
  //       case "Product#SOCKS1" => Future(EventSpan(PutProductDetails("Fluffy socks")))           //
  //       case "Product#SOCKS2" => Future(EventSpan(PutProductDetails("Coarse bristly mittens"))) //
  //     })                                                                                        //
  //                                                                                               //
  //     "should have two SKUs in it" in {                                                         //
  //       fons.view(Ref.allProducts)                                                              //
  //           .map(view => {                                                                      //
  //             assert(view.skus.lengthCompare(2) == 0)                                           //
  //           })                                                                                  //
  //     }                                                                                         //
  //                                                                                               //
  //   }                                                                                           //
  // }                                                                                             //
  //                                                                                               //
  //                                                                                               //
  // "ProductSet collects all created products" in {                                               //
  //                                                                                               //
  //   val fons = new Fons({                                                                       //
  //     case "Product#p123" => Future(EventSpan(PutProductDetails("Velvet Socks", 0.50f)))        //
  //     case "Product#p456" => Future(EventSpan(PutProductDetails("Hair Shirt", 13.33f)))         //
  //   })                                                                                          //
  //                                                                                               //
  //   fons.view(Ref.allProducts)                                                                  //
  //       .map(productSet => {                                                                    //
  //         assert(productSet.skus == Seq("p123", "p456"))                                        //
  //       })                                                                                      //
  // }                                                                                             //
  ///////////////////////////////////////////////////////////////////////////////////////////////////
  
}
