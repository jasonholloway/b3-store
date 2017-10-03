// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.woodpigeon.b3.schema.v100



object V100Proto extends _root_.com.trueaccord.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.com.trueaccord.scalapb.GeneratedFileObject] = Seq(
  )
  lazy val messagesCompanions: Seq[_root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_]] = Seq(
    com.woodpigeon.b3.schema.v100.Result,
    com.woodpigeon.b3.schema.v100.Command,
    com.woodpigeon.b3.schema.v100.Nop,
    com.woodpigeon.b3.schema.v100.PutProduct,
    com.woodpigeon.b3.schema.v100.DeleteProduct,
    com.woodpigeon.b3.schema.v100.PutStock,
    com.woodpigeon.b3.schema.v100.PutImage
  )
  private lazy val ProtoBytes: Array[Byte] =
      com.trueaccord.scalapb.Encoding.fromBase64(scala.collection.Seq(
  """Cgp2MTAwLnByb3RvEhhjb20ud29vZHBpZ2Vvbi5iMy5zY2hlbWEiKAoGUmVzdWx0Eg4KAm9rGAEgASgIUgJvaxIOCgJpZBgCI
  AEoCVICaWQixQIKB0NvbW1hbmQSDgoCaWQYASABKAlSAmlkEkYKCnB1dFByb2R1Y3QYAiABKAsyJC5jb20ud29vZHBpZ2Vvbi5iM
  y5zY2hlbWEuUHV0UHJvZHVjdEgAUgpwdXRQcm9kdWN0Ek8KDWRlbGV0ZVByb2R1Y3QYAyABKAsyJy5jb20ud29vZHBpZ2Vvbi5iM
  y5zY2hlbWEuRGVsZXRlUHJvZHVjdEgAUg1kZWxldGVQcm9kdWN0EkAKCHB1dFN0b2NrGAQgASgLMiIuY29tLndvb2RwaWdlb24uY
  jMuc2NoZW1hLlB1dFN0b2NrSABSCHB1dFN0b2NrEkAKCHB1dEltYWdlGAUgASgLMiIuY29tLndvb2RwaWdlb24uYjMuc2NoZW1hL
  lB1dEltYWdlSABSCHB1dEltYWdlQg0KC3VwZGF0ZV90eXBlIgUKA05vcCI+CgpQdXRQcm9kdWN0EhwKCXByb2R1Y3RJZBgBIAEoC
  VIJcHJvZHVjdElkEhIKBG5hbWUYAiABKAlSBG5hbWUiLQoNRGVsZXRlUHJvZHVjdBIcCglwcm9kdWN0SWQYASABKAlSCXByb2R1Y
  3RJZCIoCghQdXRTdG9jaxIcCglwcm9kdWN0SWQYASABKAlSCXByb2R1Y3RJZCI4CghQdXRJbWFnZRIaCghlbnRpdHlJZBgBIAEoC
  VIIZW50aXR5SWQSEAoDdXJsGAIgASgJUgN1cmxiBnByb3RvMw=="""
      ).mkString)
  lazy val scalaDescriptor: _root_.scalapb.descriptors.FileDescriptor = {
    val scalaProto = com.google.protobuf.descriptor.FileDescriptorProto.parseFrom(ProtoBytes)
    _root_.scalapb.descriptors.FileDescriptor.buildFrom(scalaProto, dependencies.map(_.scalaDescriptor))
  }
  lazy val javaDescriptor: com.google.protobuf.Descriptors.FileDescriptor = {
    val javaProto = com.google.protobuf.DescriptorProtos.FileDescriptorProto.parseFrom(ProtoBytes)
    com.google.protobuf.Descriptors.FileDescriptor.buildFrom(javaProto, Array(
    ))
  }
  @deprecated("Use javaDescriptor instead. In a future version this will refer to scalaDescriptor.", "ScalaPB 0.5.47")
  def descriptor: com.google.protobuf.Descriptors.FileDescriptor = javaDescriptor
}