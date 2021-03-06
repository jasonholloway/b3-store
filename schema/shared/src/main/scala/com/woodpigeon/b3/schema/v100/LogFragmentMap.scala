// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.woodpigeon.b3.schema.v100



@SerialVersionUID(0L)
final case class LogFragmentMap(
    fragments: scala.collection.immutable.Map[String, com.woodpigeon.b3.schema.v100.LogFragment] = scala.collection.immutable.Map.empty
    ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[LogFragmentMap] with com.trueaccord.lenses.Updatable[LogFragmentMap] {
    @transient
    private[this] var __serializedSizeCachedValue: Int = 0
    private[this] def __computeSerializedValue(): Int = {
      var __size = 0
      fragments.foreach(fragments => __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(com.woodpigeon.b3.schema.v100.LogFragmentMap._typemapper_fragments.toBase(fragments).serializedSize) + com.woodpigeon.b3.schema.v100.LogFragmentMap._typemapper_fragments.toBase(fragments).serializedSize)
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
      fragments.foreach { __v =>
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(com.woodpigeon.b3.schema.v100.LogFragmentMap._typemapper_fragments.toBase(__v).serializedSize)
        com.woodpigeon.b3.schema.v100.LogFragmentMap._typemapper_fragments.toBase(__v).writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.woodpigeon.b3.schema.v100.LogFragmentMap = {
      val __fragments = (scala.collection.immutable.Map.newBuilder[String, com.woodpigeon.b3.schema.v100.LogFragment] ++= this.fragments)
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __fragments += com.woodpigeon.b3.schema.v100.LogFragmentMap._typemapper_fragments.toCustom(_root_.com.trueaccord.scalapb.LiteParser.readMessage(_input__, com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry.defaultInstance))
          case tag => _input__.skipField(tag)
        }
      }
      com.woodpigeon.b3.schema.v100.LogFragmentMap(
          fragments = __fragments.result()
      )
    }
    def clearFragments = copy(fragments = scala.collection.immutable.Map.empty)
    def addFragments(__vs: (String, com.woodpigeon.b3.schema.v100.LogFragment)*): LogFragmentMap = addAllFragments(__vs)
    def addAllFragments(__vs: TraversableOnce[(String, com.woodpigeon.b3.schema.v100.LogFragment)]): LogFragmentMap = copy(fragments = fragments ++ __vs)
    def withFragments(__v: scala.collection.immutable.Map[String, com.woodpigeon.b3.schema.v100.LogFragment]): LogFragmentMap = copy(fragments = __v)
    def getFieldByNumber(__fieldNumber: Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => fragments.map(com.woodpigeon.b3.schema.v100.LogFragmentMap._typemapper_fragments.toBase)(_root_.scala.collection.breakOut)
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PRepeated(fragments.map(com.woodpigeon.b3.schema.v100.LogFragmentMap._typemapper_fragments.toBase(_).toPMessage)(_root_.scala.collection.breakOut))
      }
    }
    override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.woodpigeon.b3.schema.v100.LogFragmentMap
}

object LogFragmentMap extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.LogFragmentMap] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.LogFragmentMap] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.woodpigeon.b3.schema.v100.LogFragmentMap = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.woodpigeon.b3.schema.v100.LogFragmentMap(
      __fieldsMap.getOrElse(__fields.get(0), Nil).asInstanceOf[_root_.scala.collection.Seq[com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry]].map(com.woodpigeon.b3.schema.v100.LogFragmentMap._typemapper_fragments.toCustom)(_root_.scala.collection.breakOut)
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.woodpigeon.b3.schema.v100.LogFragmentMap] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.woodpigeon.b3.schema.v100.LogFragmentMap(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.collection.Seq[com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry]]).getOrElse(_root_.scala.collection.Seq.empty).map(com.woodpigeon.b3.schema.v100.LogFragmentMap._typemapper_fragments.toCustom)(_root_.scala.collection.breakOut)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = V100Proto.javaDescriptor.getMessageTypes.get(1)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = V100Proto.scalaDescriptor.messages(1)
  def messageCompanionForFieldNumber(__number: Int): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_]] = Seq[_root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_]](
    _root_.com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry
  )
  def enumCompanionForFieldNumber(__fieldNumber: Int): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.woodpigeon.b3.schema.v100.LogFragmentMap(
  )
  @SerialVersionUID(0L)
  final case class FragmentsEntry(
      key: String = "",
      value: scala.Option[com.woodpigeon.b3.schema.v100.LogFragment] = None
      ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[FragmentsEntry] with com.trueaccord.lenses.Updatable[FragmentsEntry] {
      @transient
      private[this] var __serializedSizeCachedValue: Int = 0
      private[this] def __computeSerializedValue(): Int = {
        var __size = 0
        if (key != "") { __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, key) }
        if (value.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(value.get.serializedSize) + value.get.serializedSize }
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
          val __v = key
          if (__v != "") {
            _output__.writeString(1, __v)
          }
        };
        value.foreach { __v =>
          _output__.writeTag(2, 2)
          _output__.writeUInt32NoTag(__v.serializedSize)
          __v.writeTo(_output__)
        };
      }
      def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry = {
        var __key = this.key
        var __value = this.value
        var _done__ = false
        while (!_done__) {
          val _tag__ = _input__.readTag()
          _tag__ match {
            case 0 => _done__ = true
            case 10 =>
              __key = _input__.readString()
            case 18 =>
              __value = Some(_root_.com.trueaccord.scalapb.LiteParser.readMessage(_input__, __value.getOrElse(com.woodpigeon.b3.schema.v100.LogFragment.defaultInstance)))
            case tag => _input__.skipField(tag)
          }
        }
        com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry(
            key = __key,
            value = __value
        )
      }
      def withKey(__v: String): FragmentsEntry = copy(key = __v)
      def getValue: com.woodpigeon.b3.schema.v100.LogFragment = value.getOrElse(com.woodpigeon.b3.schema.v100.LogFragment.defaultInstance)
      def clearValue: FragmentsEntry = copy(value = None)
      def withValue(__v: com.woodpigeon.b3.schema.v100.LogFragment): FragmentsEntry = copy(value = Some(__v))
      def getFieldByNumber(__fieldNumber: Int): scala.Any = {
        (__fieldNumber: @_root_.scala.unchecked) match {
          case 1 => {
            val __t = key
            if (__t != "") __t else null
          }
          case 2 => value.orNull
        }
      }
      def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
        require(__field.containingMessage eq companion.scalaDescriptor)
        (__field.number: @_root_.scala.unchecked) match {
          case 1 => _root_.scalapb.descriptors.PString(key)
          case 2 => value.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        }
      }
      override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)
      def companion = com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry
  }
  
  object FragmentsEntry extends com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry] {
    implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry] = this
    def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry = {
      require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
      val __fields = javaDescriptor.getFields
      com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry(
        __fieldsMap.getOrElse(__fields.get(0), "").asInstanceOf[String],
        __fieldsMap.get(__fields.get(1)).asInstanceOf[scala.Option[com.woodpigeon.b3.schema.v100.LogFragment]]
      )
    }
    implicit def messageReads: _root_.scalapb.descriptors.Reads[com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry] = _root_.scalapb.descriptors.Reads{
      case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
        require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
        com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry(
          __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[String]).getOrElse(""),
          __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[scala.Option[com.woodpigeon.b3.schema.v100.LogFragment]])
        )
      case _ => throw new RuntimeException("Expected PMessage")
    }
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = com.woodpigeon.b3.schema.v100.LogFragmentMap.javaDescriptor.getNestedTypes.get(0)
    def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = com.woodpigeon.b3.schema.v100.LogFragmentMap.scalaDescriptor.nestedMessages(0)
    def messageCompanionForFieldNumber(__number: Int): _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = {
      var __out: _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = null
      (__number: @_root_.scala.unchecked) match {
        case 2 => __out = com.woodpigeon.b3.schema.v100.LogFragment
      }
      __out
    }
    lazy val nestedMessagesCompanions: Seq[_root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
    def enumCompanionForFieldNumber(__fieldNumber: Int): _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
    lazy val defaultInstance = com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry(
    )
    implicit class FragmentsEntryLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry](_l) {
      def key: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.key)((c_, f_) => c_.copy(key = f_))
      def value: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.LogFragment] = field(_.getValue)((c_, f_) => c_.copy(value = Some(f_)))
      def optionalValue: _root_.com.trueaccord.lenses.Lens[UpperPB, scala.Option[com.woodpigeon.b3.schema.v100.LogFragment]] = field(_.value)((c_, f_) => c_.copy(value = f_))
    }
    final val KEY_FIELD_NUMBER = 1
    final val VALUE_FIELD_NUMBER = 2
    implicit val keyValueMapper: _root_.com.trueaccord.scalapb.TypeMapper[com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry, (String, com.woodpigeon.b3.schema.v100.LogFragment)] =
      _root_.com.trueaccord.scalapb.TypeMapper[com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry, (String, com.woodpigeon.b3.schema.v100.LogFragment)](__m => (__m.key, __m.getValue))(__p => com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry(__p._1, Some(__p._2)))
  }
  
  implicit class LogFragmentMapLens[UpperPB](_l: _root_.com.trueaccord.lenses.Lens[UpperPB, com.woodpigeon.b3.schema.v100.LogFragmentMap]) extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, com.woodpigeon.b3.schema.v100.LogFragmentMap](_l) {
    def fragments: _root_.com.trueaccord.lenses.Lens[UpperPB, scala.collection.immutable.Map[String, com.woodpigeon.b3.schema.v100.LogFragment]] = field(_.fragments)((c_, f_) => c_.copy(fragments = f_))
  }
  final val FRAGMENTS_FIELD_NUMBER = 1
  @transient
  private val _typemapper_fragments: _root_.com.trueaccord.scalapb.TypeMapper[com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry, (String, com.woodpigeon.b3.schema.v100.LogFragment)] = implicitly[_root_.com.trueaccord.scalapb.TypeMapper[com.woodpigeon.b3.schema.v100.LogFragmentMap.FragmentsEntry, (String, com.woodpigeon.b3.schema.v100.LogFragment)]]
}
