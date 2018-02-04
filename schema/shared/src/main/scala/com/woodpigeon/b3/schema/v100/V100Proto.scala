// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.woodpigeon.b3.schema.v100



object V100Proto extends _root_.com.trueaccord.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.com.trueaccord.scalapb.GeneratedFileObject] = Seq(
  )
  lazy val messagesCompanions: Seq[_root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_]] = Seq(
    com.woodpigeon.b3.schema.v100.LogFragment,
    com.woodpigeon.b3.schema.v100.LogFragmentMap,
    com.woodpigeon.b3.schema.v100.LogOffsetMap,
    com.woodpigeon.b3.schema.v100.Event,
    com.woodpigeon.b3.schema.v100.NoteList,
    com.woodpigeon.b3.schema.v100.ProductView,
    com.woodpigeon.b3.schema.v100.ProductSetView,
    com.woodpigeon.b3.schema.v100.AddNote,
    com.woodpigeon.b3.schema.v100.PutProduct,
    com.woodpigeon.b3.schema.v100.DeleteProduct,
    com.woodpigeon.b3.schema.v100.PutStock,
    com.woodpigeon.b3.schema.v100.PutImage,
    com.woodpigeon.b3.schema.v100.PutProductDetails,
    com.woodpigeon.b3.schema.v100.AnnounceProduct
  )
  private lazy val ProtoBytes: Array[Byte] =
      com.trueaccord.scalapb.Encoding.fromBase64(scala.collection.Seq(
  """CiNzaGFyZWQvc3JjL21haW4vcHJvdG9idWYvdjEwMC5wcm90bxIYY29tLndvb2RwaWdlb24uYjMuc2NoZW1hIl4KC0xvZ0ZyY
  WdtZW50EhYKBm9mZnNldBgBIAEoDVIGb2Zmc2V0EjcKBmV2ZW50cxgCIAMoCzIfLmNvbS53b29kcGlnZW9uLmIzLnNjaGVtYS5Fd
  mVudFIGZXZlbnRzIswBCg5Mb2dGcmFnbWVudE1hcBJVCglmcmFnbWVudHMYASADKAsyNy5jb20ud29vZHBpZ2Vvbi5iMy5zY2hlb
  WEuTG9nRnJhZ21lbnRNYXAuRnJhZ21lbnRzRW50cnlSCWZyYWdtZW50cxpjCg5GcmFnbWVudHNFbnRyeRIQCgNrZXkYASABKAlSA
  2tleRI7CgV2YWx1ZRgCIAEoCzIlLmNvbS53b29kcGlnZW9uLmIzLnNjaGVtYS5Mb2dGcmFnbWVudFIFdmFsdWU6AjgBIpkBCgxMb
  2dPZmZzZXRNYXASTQoHb2Zmc2V0cxgBIAMoCzIzLmNvbS53b29kcGlnZW9uLmIzLnNjaGVtYS5Mb2dPZmZzZXRNYXAuT2Zmc2V0c
  0VudHJ5UgdvZmZzZXRzGjoKDE9mZnNldHNFbnRyeRIQCgNrZXkYASABKAlSA2tleRIUCgV2YWx1ZRgCIAEoDVIFdmFsdWU6AjgBI
  ssCCgVFdmVudBI9CgdhZGROb3RlGAcgASgLMiEuY29tLndvb2RwaWdlb24uYjMuc2NoZW1hLkFkZE5vdGVIAFIHYWRkTm90ZRJGC
  gpwdXRQcm9kdWN0GAggASgLMiQuY29tLndvb2RwaWdlb24uYjMuc2NoZW1hLlB1dFByb2R1Y3RIAFIKcHV0UHJvZHVjdBJbChFwd
  XRQcm9kdWN0RGV0YWlscxgJIAEoCzIrLmNvbS53b29kcGlnZW9uLmIzLnNjaGVtYS5QdXRQcm9kdWN0RGV0YWlsc0gAUhFwdXRQc
  m9kdWN0RGV0YWlscxJVCg9hbm5vdW5jZVByb2R1Y3QYCiABKAsyKS5jb20ud29vZHBpZ2Vvbi5iMy5zY2hlbWEuQW5ub3VuY2VQc
  m9kdWN0SABSD2Fubm91bmNlUHJvZHVjdEIHCgVpbm5lciIgCghOb3RlTGlzdBIUCgVub3RlcxgBIAMoCVIFbm90ZXMiSQoLUHJvZ
  HVjdFZpZXcSEAoDc2t1GAEgASgJUgNza3USEgoEbmFtZRgCIAEoCVIEbmFtZRIUCgVwcmljZRgDIAEoAlIFcHJpY2UiJAoOUHJvZ
  HVjdFNldFZpZXcSEgoEc2t1cxgBIAMoCVIEc2t1cyIdCgdBZGROb3RlEhIKBG5vdGUYASABKAlSBG5vdGUiPgoKUHV0UHJvZHVjd
  BIcCglwcm9kdWN0SWQYASABKAlSCXByb2R1Y3RJZBISCgRuYW1lGAIgASgJUgRuYW1lIi0KDURlbGV0ZVByb2R1Y3QSHAoJcHJvZ
  HVjdElkGAEgASgJUglwcm9kdWN0SWQiKAoIUHV0U3RvY2sSHAoJcHJvZHVjdElkGAEgASgJUglwcm9kdWN0SWQiOAoIUHV0SW1hZ
  2USGgoIZW50aXR5SWQYASABKAlSCGVudGl0eUlkEhAKA3VybBgCIAEoCVIDdXJsIj0KEVB1dFByb2R1Y3REZXRhaWxzEhIKBG5hb
  WUYASABKAlSBG5hbWUSFAoFcHJpY2UYAiABKAJSBXByaWNlIiMKD0Fubm91bmNlUHJvZHVjdBIQCgNza3UYASABKAlSA3NrdWIGc
  HJvdG8z"""
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