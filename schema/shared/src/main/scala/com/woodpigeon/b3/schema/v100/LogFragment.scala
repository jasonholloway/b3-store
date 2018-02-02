// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.woodpigeon.b3.schema.v100



@SerialVersionUID(0L)
final case class LogFragment(
    offset: Int = 0,
    events: _root_.scala.collection.Seq[com.woodpigeon.b3.schema.v100.Event] = _root_.scala.collection.Seq.empty
    ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[LogFragment] with com.trueaccord.lenses.Updatable[LogFragment] {
    @transient
    private[this] var __serializedSizeCachedValue: Int = 0
    private[this] def __computeSerializedValue(): Int = {
      var __size = 0
      if (offset != 0) { __size += _root_.com.google.protobuf.CodedOutputStream.computeUInt32Size(1, offset) }
      events.foreach(events => __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(events.serializedSize) + events.serializedSize)
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
        val __v = offset
        if (__v != 0) {
          _output__.writeUInt32(1, __v)
        }
      };
      events.foreach { __v =>
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.woodpigeon.b3.schema.v100.LogFragment = {
      var __offset = this.offset
      val __events = (_root_.scala.collection.immutable.Vector.newBuilder[com.woodpigeon.b3.schema.v100.Event] ++= this.events)
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __offset = _input__.readUInt32()
          case 18 =>
            __events += _root_.com.trueaccord.scalapb.LiteParser.readMessage(_input__, com.woodpigeon.b3.schema.v100.Event.defaultInstance)
          case tag => _input__.skipField(tag)
        }
      }
      com.woodpigeon.b3.schema.v100.LogFragment(
          offset = __offset,
          events = __events.result()
      )
    }
    def withOffset(__v: Int): LogFragment = copy(offset = __v)
    def clearEvents = copy(events = _root_.scala.collection.Seq.empty)
    def addEvents(__vs: com.woodpigeon.b3.schema.v100.Event*): LogFragment = addAllEvents(__vs)
    def addAllEvents(__vs: TraversableOnce[com.woodpigeon.b3.schema.v100.Event]): LogFragment = copy(events = events ++ __vs)
    def withEvents(__v: _root_.scala.collection.Seq[com.woodpigeon.b3.schema.v100.Event]): LogFragment = copy(events = __v)
    def getFieldByNumber(__fieldNumber: Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = offset
          if (__t != 0) __t else null
        }
        case 2 => events
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(offset)
        case 2 => _root_.scalapb.descriptors.PRepeated(events.map(_.toPMessage)(_root_.scala.collection.breakOut))
      }
    }
    override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.woodpigeon.b3.schema.v100.LogFragment
}

object LogFragment extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.LogFragment] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.LogFragment] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.woodpigeon.b3.schema.v100.LogFragment = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.woodpigeon.b3.schema.v100.LogFragment(
      __fieldsMap.getOrElse(__fields.get(0), 0).asInstanceOf[Int],
      __fieldsMap.getOrElse(__fields.get(1), Nil).asInstanceOf[_root_.scala.collection.Seq[com.woodpigeon.b3.schema.v100.Event]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.woodpigeon.b3.schema.v100.LogFragment] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.woodpigeon.b3.schema.v100.LogFragment(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[Int]).getOrElse(0),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.collection.Seq[com.woodpigeon.b3.schema.v100.Event]]).getOrElse(_root_.scala.collection.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = V100Proto.javaDescriptor.getMessageTypes.get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = V100Proto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: Int): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 2 => __out = com.woodpigeon.b3.schema.v100.Event
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: Int): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.woodpigeon.b3.schema.v100.LogFragment(
  )
  implicit class LogFragmentLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.LogFragment]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.woodpigeon.b3.schema.v100.LogFragment](_l) {
    def offset: _root_.com.trueaccord.lenses.Lens[UpperPB, Int] = field(_.offset)((c_, f_) => c_.copy(offset = f_))
    def events: _root_.com.trueaccord.lenses.Lens[UpperPB, _root_.scala.collection.Seq[com.woodpigeon.b3.schema.v100.Event]] = field(_.events)((c_, f_) => c_.copy(events = f_))
  }
  final val OFFSET_FIELD_NUMBER = 1
  final val EVENTS_FIELD_NUMBER = 2
}
