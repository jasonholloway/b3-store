// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.woodpigeon.b3.schema.v100



@SerialVersionUID(0L)
final case class ProductInfo(
    sku: String = "",
    name: String = "",
    price: Float = 0.0f
    ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[ProductInfo] with com.trueaccord.lenses.Updatable[ProductInfo] {
    @transient
    private[this] var __serializedSizeCachedValue: Int = 0
    private[this] def __computeSerializedValue(): Int = {
      var __size = 0
      if (sku != "") { __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, sku) }
      if (name != "") { __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, name) }
      if (price != 0.0f) { __size += _root_.com.google.protobuf.CodedOutputStream.computeFloatSize(3, price) }
      __size
    }
    final override def serializedSize: Int = {
      var read = __serializedSizeCachedValue
      if (read == 0) {
        read = __computeSerializedValue()
        __serializedSizeCachedValue = read
      }
      read
    }
    def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): Unit = {
      {
        val __v = sku
        if (__v != "") {
          _output__.writeString(1, __v)
        }
      };
      {
        val __v = name
        if (__v != "") {
          _output__.writeString(2, __v)
        }
      };
      {
        val __v = price
        if (__v != 0.0f) {
          _output__.writeFloat(3, __v)
        }
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.woodpigeon.b3.schema.v100.ProductInfo = {
      var __sku = this.sku
      var __name = this.name
      var __price = this.price
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __sku = _input__.readString()
          case 18 =>
            __name = _input__.readString()
          case 29 =>
            __price = _input__.readFloat()
          case tag => _input__.skipField(tag)
        }
      }
      com.woodpigeon.b3.schema.v100.ProductInfo(
          sku = __sku,
          name = __name,
          price = __price
      )
    }
    def withSku(__v: String): ProductInfo = copy(sku = __v)
    def withName(__v: String): ProductInfo = copy(name = __v)
    def withPrice(__v: Float): ProductInfo = copy(price = __v)
    def getFieldByNumber(__fieldNumber: Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = sku
          if (__t != "") __t else null
        }
        case 2 => {
          val __t = name
          if (__t != "") __t else null
        }
        case 3 => {
          val __t = price
          if (__t != 0.0f) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(sku)
        case 2 => _root_.scalapb.descriptors.PString(name)
        case 3 => _root_.scalapb.descriptors.PFloat(price)
      }
    }
    override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.woodpigeon.b3.schema.v100.ProductInfo
}

object ProductInfo extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.ProductInfo] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.ProductInfo] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.woodpigeon.b3.schema.v100.ProductInfo = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.woodpigeon.b3.schema.v100.ProductInfo(
      __fieldsMap.getOrElse(__fields.get(0), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(1), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(2), 0.0f).asInstanceOf[Float]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.woodpigeon.b3.schema.v100.ProductInfo] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.woodpigeon.b3.schema.v100.ProductInfo(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[String]).getOrElse(""),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[String]).getOrElse(""),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[Float]).getOrElse(0.0f)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = V100Proto.javaDescriptor.getMessageTypes.get(5)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = V100Proto.scalaDescriptor.messages(5)
  def messageCompanionForFieldNumber(__number: Int): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: Int): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.woodpigeon.b3.schema.v100.ProductInfo(
  )
  implicit class ProductInfoLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.ProductInfo]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.woodpigeon.b3.schema.v100.ProductInfo](_l) {
    def sku: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.sku)((c_, f_) => c_.copy(sku = f_))
    def name: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.name)((c_, f_) => c_.copy(name = f_))
    def price: _root_.com.trueaccord.lenses.Lens[UpperPB, Float] = field(_.price)((c_, f_) => c_.copy(price = f_))
  }
  final val SKU_FIELD_NUMBER = 1
  final val NAME_FIELD_NUMBER = 2
  final val PRICE_FIELD_NUMBER = 3
}
