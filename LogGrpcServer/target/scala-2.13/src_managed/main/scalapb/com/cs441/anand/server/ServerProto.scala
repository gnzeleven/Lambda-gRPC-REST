// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.cs441.anand.server

object ServerProto extends _root_.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.scalapb.GeneratedFileObject] = Seq.empty
  lazy val messagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] =
    Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]](
      com.cs441.anand.server.HelloRequest,
      com.cs441.anand.server.HelloReply
    )
  private lazy val ProtoBytes: _root_.scala.Array[Byte] =
      scalapb.Encoding.fromBase64(scala.collection.immutable.Seq(
  """CgxzZXJ2ZXIucHJvdG8SD2NvbS5jczQ0MS5hbmFuZCItCgxIZWxsb1JlcXVlc3QSHQoEbmFtZRgBIAEoCUIJ4j8GEgRuYW1lU
  gRuYW1lIjQKCkhlbGxvUmVwbHkSJgoHbWVzc2FnZRgBIAEoCUIM4j8JEgdtZXNzYWdlUgdtZXNzYWdlMlMKB0dyZWV0ZXISSAoIU
  2F5SGVsbG8SHS5jb20uY3M0NDEuYW5hbmQuSGVsbG9SZXF1ZXN0GhsuY29tLmNzNDQxLmFuYW5kLkhlbGxvUmVwbHkiAGIGcHJvd
  G8z"""
      ).mkString)
  lazy val scalaDescriptor: _root_.scalapb.descriptors.FileDescriptor = {
    val scalaProto = com.google.protobuf.descriptor.FileDescriptorProto.parseFrom(ProtoBytes)
    _root_.scalapb.descriptors.FileDescriptor.buildFrom(scalaProto, dependencies.map(_.scalaDescriptor))
  }
  lazy val javaDescriptor: com.google.protobuf.Descriptors.FileDescriptor = {
    val javaProto = com.google.protobuf.DescriptorProtos.FileDescriptorProto.parseFrom(ProtoBytes)
    com.google.protobuf.Descriptors.FileDescriptor.buildFrom(javaProto, _root_.scala.Array(
    ))
  }
  @deprecated("Use javaDescriptor instead. In a future version this will refer to scalaDescriptor.", "ScalaPB 0.5.47")
  def descriptor: com.google.protobuf.Descriptors.FileDescriptor = javaDescriptor
}