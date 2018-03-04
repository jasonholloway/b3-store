package com.woodpigeon.b3

import com.woodpigeon.b3.schema.v100.{ProductSetView, ProductView}

trait Entity {
  type Key
  type View
}

sealed trait Product extends Entity { type Key = SKU; type View = ProductView }

sealed trait ProductSet extends Entity { type Key = String; type View = ProductSetView }
