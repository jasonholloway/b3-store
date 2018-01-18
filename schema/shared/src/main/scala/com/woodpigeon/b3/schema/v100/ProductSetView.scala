// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.woodpigeon.b3.schema.v100



@SerialVersionUID(0L)
final case class ProductSetView(
    skus: _root_.scala.collection.Seq[String] = _root_.scala.collection.Seq.empty
    ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[ProductSetView] with com.trueaccord.lenses.Updatable[ProductSetView] {
    @transient
    private[this] var __serializedSizeCachedValue: Int = 0
    private[this] def __computeSerializedValue(): Int = {
      var __size = 0
      skus.foreach(skus => __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, skus))
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
      skus.foreach { __v =>
        _output__.writeString(1, __v)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.woodpigeon.b3.schema.v100.ProductSetView = {
      val __skus = (_root_.scala.collection.immutable.Vector.newBuilder[String] ++= this.skus)
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __skus += _input__.readString()
          case tag => _input__.skipField(tag)
        }
      }
      com.woodpigeon.b3.schema.v100.ProductSetView(
          skus = __skus.result()
      )
    }
    def clearSkus = copy(skus = _root_.scala.collection.Seq.empty)
    def addSkus(__vs: String*): ProductSetView = addAllSkus(__vs)
    def addAllSkus(__vs: TraversableOnce[String]): ProductSetView = copy(skus = skus ++ __vs)
    def withSkus(__v: _root_.scala.collection.Seq[String]): ProductSetView = copy(skus = __v)
    def getFieldByNumber(__fieldNumber: Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => skus
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PRepeated(skus.map(_root_.scalapb.descriptors.PString)(_root_.scala.collection.breakOut))
      }
    }
    override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.woodpigeon.b3.schema.v100.ProductSetView
}

object ProductSetView extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.ProductSetView] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.ProductSetView] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.woodpigeon.b3.schema.v100.ProductSetView = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.woodpigeon.b3.schema.v100.ProductSetView(
      __fieldsMap.getOrElse(__fields.get(0), Nil).asInstanceOf[_root_.scala.collection.Seq[String]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.woodpigeon.b3.schema.v100.ProductSetView] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.woodpigeon.b3.schema.v100.ProductSetView(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.collection.Seq[String]]).getOrElse(_root_.scala.collection.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = V100Proto.javaDescriptor.getMessageTypes.get(6)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = V100Proto.scalaDescriptor.messages(6)
  def messageCompanionForFieldNumber(__number: Int): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: Int): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.woodpigeon.b3.schema.v100.ProductSetView(
  )
  implicit class ProductSetViewLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.ProductSetView]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.woodpigeon.b3.schema.v100.ProductSetView](_l) {
    def skus: _root_.com.trueaccord.lenses.Lens[UpperPB, _root_.scala.collection.Seq[String]] = field(_.skus)((c_, f_) => c_.copy(skus = f_))
  }
  final val SKUS_FIELD_NUMBER = 1
}
