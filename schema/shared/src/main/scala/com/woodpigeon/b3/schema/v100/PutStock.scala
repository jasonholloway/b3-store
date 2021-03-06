// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.woodpigeon.b3.schema.v100



@SerialVersionUID(0L)
final case class PutStock(
    productId: String = ""
    ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[PutStock] with com.trueaccord.lenses.Updatable[PutStock] {
    @transient
    private[this] var __serializedSizeCachedValue: Int = 0
    private[this] def __computeSerializedValue(): Int = {
      var __size = 0
      if (productId != "") { __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, productId) }
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
        val __v = productId
        if (__v != "") {
          _output__.writeString(1, __v)
        }
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.woodpigeon.b3.schema.v100.PutStock = {
      var __productId = this.productId
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __productId = _input__.readString()
          case tag => _input__.skipField(tag)
        }
      }
      com.woodpigeon.b3.schema.v100.PutStock(
          productId = __productId
      )
    }
    def withProductId(__v: String): PutStock = copy(productId = __v)
    def getFieldByNumber(__fieldNumber: Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = productId
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(productId)
      }
    }
    override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.woodpigeon.b3.schema.v100.PutStock
}

object PutStock extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.PutStock] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.PutStock] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.woodpigeon.b3.schema.v100.PutStock = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.woodpigeon.b3.schema.v100.PutStock(
      __fieldsMap.getOrElse(__fields.get(0), "").asInstanceOf[String]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.woodpigeon.b3.schema.v100.PutStock] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.woodpigeon.b3.schema.v100.PutStock(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = V100Proto.javaDescriptor.getMessageTypes.get(10)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = V100Proto.scalaDescriptor.messages(10)
  def messageCompanionForFieldNumber(__number: Int): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: Int): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.woodpigeon.b3.schema.v100.PutStock(
  )
  implicit class PutStockLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.PutStock]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.woodpigeon.b3.schema.v100.PutStock](_l) {
    def productId: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.productId)((c_, f_) => c_.copy(productId = f_))
  }
  final val PRODUCTID_FIELD_NUMBER = 1
}
