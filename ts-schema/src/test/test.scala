package Ducks

import woodpigeon.tsSchema._

@model
class Duck(bill: Boolean)

@model
case class Mallard(bill: Boolean, blahblah: Integer) extends Duck(bill)
