// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.woodpigeon.b3.schema.v100



@SerialVersionUID(0L)
final case class Event(
    inner: com.woodpigeon.b3.schema.v100.Event.Inner = com.woodpigeon.b3.schema.v100.Event.Inner.Empty
    ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[Event] with com.trueaccord.lenses.Updatable[Event] {
    @transient
    private[this] var __serializedSizeCachedValue: Int = 0
    private[this] def __computeSerializedValue(): Int = {
      var __size = 0
      if (inner.addNote.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(inner.addNote.get.serializedSize) + inner.addNote.get.serializedSize }
      if (inner.putProduct.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(inner.putProduct.get.serializedSize) + inner.putProduct.get.serializedSize }
      if (inner.putProductDetails.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(inner.putProductDetails.get.serializedSize) + inner.putProductDetails.get.serializedSize }
      if (inner.announceProduct.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(inner.announceProduct.get.serializedSize) + inner.announceProduct.get.serializedSize }
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
      inner.addNote.foreach { __v =>
        _output__.writeTag(7, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
      inner.putProduct.foreach { __v =>
        _output__.writeTag(8, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
      inner.putProductDetails.foreach { __v =>
        _output__.writeTag(9, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
      inner.announceProduct.foreach { __v =>
        _output__.writeTag(10, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.woodpigeon.b3.schema.v100.Event = {
      var __inner = this.inner
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 58 =>
            __inner = com.woodpigeon.b3.schema.v100.Event.Inner.AddNote(_root_.com.trueaccord.scalapb.LiteParser.readMessage(_input__, inner.addNote.getOrElse(com.woodpigeon.b3.schema.v100.AddNote.defaultInstance)))
          case 66 =>
            __inner = com.woodpigeon.b3.schema.v100.Event.Inner.PutProduct(_root_.com.trueaccord.scalapb.LiteParser.readMessage(_input__, inner.putProduct.getOrElse(com.woodpigeon.b3.schema.v100.PutProduct.defaultInstance)))
          case 74 =>
            __inner = com.woodpigeon.b3.schema.v100.Event.Inner.PutProductDetails(_root_.com.trueaccord.scalapb.LiteParser.readMessage(_input__, inner.putProductDetails.getOrElse(com.woodpigeon.b3.schema.v100.PutProductDetails.defaultInstance)))
          case 82 =>
            __inner = com.woodpigeon.b3.schema.v100.Event.Inner.AnnounceProduct(_root_.com.trueaccord.scalapb.LiteParser.readMessage(_input__, inner.announceProduct.getOrElse(com.woodpigeon.b3.schema.v100.AnnounceProduct.defaultInstance)))
          case tag => _input__.skipField(tag)
        }
      }
      com.woodpigeon.b3.schema.v100.Event(
          inner = __inner
      )
    }
    def getAddNote: com.woodpigeon.b3.schema.v100.AddNote = inner.addNote.getOrElse(com.woodpigeon.b3.schema.v100.AddNote.defaultInstance)
    def withAddNote(__v: com.woodpigeon.b3.schema.v100.AddNote): Event = copy(inner = com.woodpigeon.b3.schema.v100.Event.Inner.AddNote(__v))
    def getPutProduct: com.woodpigeon.b3.schema.v100.PutProduct = inner.putProduct.getOrElse(com.woodpigeon.b3.schema.v100.PutProduct.defaultInstance)
    def withPutProduct(__v: com.woodpigeon.b3.schema.v100.PutProduct): Event = copy(inner = com.woodpigeon.b3.schema.v100.Event.Inner.PutProduct(__v))
    def getPutProductDetails: com.woodpigeon.b3.schema.v100.PutProductDetails = inner.putProductDetails.getOrElse(com.woodpigeon.b3.schema.v100.PutProductDetails.defaultInstance)
    def withPutProductDetails(__v: com.woodpigeon.b3.schema.v100.PutProductDetails): Event = copy(inner = com.woodpigeon.b3.schema.v100.Event.Inner.PutProductDetails(__v))
    def getAnnounceProduct: com.woodpigeon.b3.schema.v100.AnnounceProduct = inner.announceProduct.getOrElse(com.woodpigeon.b3.schema.v100.AnnounceProduct.defaultInstance)
    def withAnnounceProduct(__v: com.woodpigeon.b3.schema.v100.AnnounceProduct): Event = copy(inner = com.woodpigeon.b3.schema.v100.Event.Inner.AnnounceProduct(__v))
    def clearInner: Event = copy(inner = com.woodpigeon.b3.schema.v100.Event.Inner.Empty)
    def withInner(__v: com.woodpigeon.b3.schema.v100.Event.Inner): Event = copy(inner = __v)
    def getFieldByNumber(__fieldNumber: Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 7 => inner.addNote.orNull
        case 8 => inner.putProduct.orNull
        case 9 => inner.putProductDetails.orNull
        case 10 => inner.announceProduct.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 7 => inner.addNote.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 8 => inner.putProduct.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 9 => inner.putProductDetails.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 10 => inner.announceProduct.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.woodpigeon.b3.schema.v100.Event
}

object Event extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.Event] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.Event] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.woodpigeon.b3.schema.v100.Event = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.woodpigeon.b3.schema.v100.Event(
      inner = __fieldsMap.get(__fields.get(0)).asInstanceOf[scala.Option[com.woodpigeon.b3.schema.v100.AddNote]].map(com.woodpigeon.b3.schema.v100.Event.Inner.AddNote)
    .orElse[com.woodpigeon.b3.schema.v100.Event.Inner](__fieldsMap.get(__fields.get(1)).asInstanceOf[scala.Option[com.woodpigeon.b3.schema.v100.PutProduct]].map(com.woodpigeon.b3.schema.v100.Event.Inner.PutProduct))
    .orElse[com.woodpigeon.b3.schema.v100.Event.Inner](__fieldsMap.get(__fields.get(2)).asInstanceOf[scala.Option[com.woodpigeon.b3.schema.v100.PutProductDetails]].map(com.woodpigeon.b3.schema.v100.Event.Inner.PutProductDetails))
    .orElse[com.woodpigeon.b3.schema.v100.Event.Inner](__fieldsMap.get(__fields.get(3)).asInstanceOf[scala.Option[com.woodpigeon.b3.schema.v100.AnnounceProduct]].map(com.woodpigeon.b3.schema.v100.Event.Inner.AnnounceProduct))
    .getOrElse(com.woodpigeon.b3.schema.v100.Event.Inner.Empty)
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.woodpigeon.b3.schema.v100.Event] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.woodpigeon.b3.schema.v100.Event(
        inner = __fieldsMap.get(scalaDescriptor.findFieldByNumber(7).get).flatMap(_.as[scala.Option[com.woodpigeon.b3.schema.v100.AddNote]]).map(com.woodpigeon.b3.schema.v100.Event.Inner.AddNote)
    .orElse[com.woodpigeon.b3.schema.v100.Event.Inner](__fieldsMap.get(scalaDescriptor.findFieldByNumber(8).get).flatMap(_.as[scala.Option[com.woodpigeon.b3.schema.v100.PutProduct]]).map(com.woodpigeon.b3.schema.v100.Event.Inner.PutProduct))
    .orElse[com.woodpigeon.b3.schema.v100.Event.Inner](__fieldsMap.get(scalaDescriptor.findFieldByNumber(9).get).flatMap(_.as[scala.Option[com.woodpigeon.b3.schema.v100.PutProductDetails]]).map(com.woodpigeon.b3.schema.v100.Event.Inner.PutProductDetails))
    .orElse[com.woodpigeon.b3.schema.v100.Event.Inner](__fieldsMap.get(scalaDescriptor.findFieldByNumber(10).get).flatMap(_.as[scala.Option[com.woodpigeon.b3.schema.v100.AnnounceProduct]]).map(com.woodpigeon.b3.schema.v100.Event.Inner.AnnounceProduct))
    .getOrElse(com.woodpigeon.b3.schema.v100.Event.Inner.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = V100Proto.javaDescriptor.getMessageTypes.get(3)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = V100Proto.scalaDescriptor.messages(3)
  def messageCompanionForFieldNumber(__number: Int): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 7 => __out = com.woodpigeon.b3.schema.v100.AddNote
      case 8 => __out = com.woodpigeon.b3.schema.v100.PutProduct
      case 9 => __out = com.woodpigeon.b3.schema.v100.PutProductDetails
      case 10 => __out = com.woodpigeon.b3.schema.v100.AnnounceProduct
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: Int): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.woodpigeon.b3.schema.v100.Event(
  )
  sealed trait Inner extends _root_.com.trueaccord.scalapb.GeneratedOneof {
    def isEmpty: Boolean = false
    def isDefined: Boolean = true
    def isAddNote: Boolean = false
    def isPutProduct: Boolean = false
    def isPutProductDetails: Boolean = false
    def isAnnounceProduct: Boolean = false
    def addNote: scala.Option[com.woodpigeon.b3.schema.v100.AddNote] = None
    def putProduct: scala.Option[com.woodpigeon.b3.schema.v100.PutProduct] = None
    def putProductDetails: scala.Option[com.woodpigeon.b3.schema.v100.PutProductDetails] = None
    def announceProduct: scala.Option[com.woodpigeon.b3.schema.v100.AnnounceProduct] = None
  }
  object Inner extends {
    @SerialVersionUID(0L)
    case object Empty extends com.woodpigeon.b3.schema.v100.Event.Inner {
      override def isEmpty: Boolean = true
      override def isDefined: Boolean = false
      override def number: Int = 0
      override def value: scala.Any = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    case class AddNote(value: com.woodpigeon.b3.schema.v100.AddNote) extends com.woodpigeon.b3.schema.v100.Event.Inner {
      override def isAddNote: Boolean = true
      override def addNote: scala.Option[com.woodpigeon.b3.schema.v100.AddNote] = Some(value)
      override def number: Int = 7
    }
    @SerialVersionUID(0L)
    case class PutProduct(value: com.woodpigeon.b3.schema.v100.PutProduct) extends com.woodpigeon.b3.schema.v100.Event.Inner {
      override def isPutProduct: Boolean = true
      override def putProduct: scala.Option[com.woodpigeon.b3.schema.v100.PutProduct] = Some(value)
      override def number: Int = 8
    }
    @SerialVersionUID(0L)
    case class PutProductDetails(value: com.woodpigeon.b3.schema.v100.PutProductDetails) extends com.woodpigeon.b3.schema.v100.Event.Inner {
      override def isPutProductDetails: Boolean = true
      override def putProductDetails: scala.Option[com.woodpigeon.b3.schema.v100.PutProductDetails] = Some(value)
      override def number: Int = 9
    }
    @SerialVersionUID(0L)
    case class AnnounceProduct(value: com.woodpigeon.b3.schema.v100.AnnounceProduct) extends com.woodpigeon.b3.schema.v100.Event.Inner {
      override def isAnnounceProduct: Boolean = true
      override def announceProduct: scala.Option[com.woodpigeon.b3.schema.v100.AnnounceProduct] = Some(value)
      override def number: Int = 10
    }
  }
  implicit class EventLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.Event]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.woodpigeon.b3.schema.v100.Event](_l) {
    def addNote: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.AddNote] = field(_.getAddNote)((c_, f_) => c_.copy(inner = com.woodpigeon.b3.schema.v100.Event.Inner.AddNote(f_)))
    def putProduct: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.PutProduct] = field(_.getPutProduct)((c_, f_) => c_.copy(inner = com.woodpigeon.b3.schema.v100.Event.Inner.PutProduct(f_)))
    def putProductDetails: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.PutProductDetails] = field(_.getPutProductDetails)((c_, f_) => c_.copy(inner = com.woodpigeon.b3.schema.v100.Event.Inner.PutProductDetails(f_)))
    def announceProduct: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.AnnounceProduct] = field(_.getAnnounceProduct)((c_, f_) => c_.copy(inner = com.woodpigeon.b3.schema.v100.Event.Inner.AnnounceProduct(f_)))
    def inner: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.Event.Inner] = field(_.inner)((c_, f_) => c_.copy(inner = f_))
  }
  final val ADDNOTE_FIELD_NUMBER = 7
  final val PUTPRODUCT_FIELD_NUMBER = 8
  final val PUTPRODUCTDETAILS_FIELD_NUMBER = 9
  final val ANNOUNCEPRODUCT_FIELD_NUMBER = 10
}
