package com.cs441.anand.server

object GreeterGrpc {
  val METHOD_DISPLAY_TIME: _root_.io.grpc.MethodDescriptor[com.cs441.anand.server.TimeRequest, com.cs441.anand.server.TimeReply] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("com.cs441.anand.Greeter", "displayTime"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.cs441.anand.server.TimeRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.cs441.anand.server.TimeReply])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.cs441.anand.server.ServerProto.javaDescriptor.getServices().get(0).getMethods().get(0)))
      .build()
  
  val SERVICE: _root_.io.grpc.ServiceDescriptor =
    _root_.io.grpc.ServiceDescriptor.newBuilder("com.cs441.anand.Greeter")
      .setSchemaDescriptor(new _root_.scalapb.grpc.ConcreteProtoFileDescriptorSupplier(com.cs441.anand.server.ServerProto.javaDescriptor))
      .addMethod(METHOD_DISPLAY_TIME)
      .build()
  
  /** The greeting service definition.
    */
  trait Greeter extends _root_.scalapb.grpc.AbstractService {
    override def serviceCompanion = Greeter
    /** Sends a greeting
      */
    def displayTime(request: com.cs441.anand.server.TimeRequest): scala.concurrent.Future[com.cs441.anand.server.TimeReply]
  }
  
  object Greeter extends _root_.scalapb.grpc.ServiceCompanion[Greeter] {
    implicit def serviceCompanion: _root_.scalapb.grpc.ServiceCompanion[Greeter] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.cs441.anand.server.ServerProto.javaDescriptor.getServices().get(0)
    def scalaDescriptor: _root_.scalapb.descriptors.ServiceDescriptor = com.cs441.anand.server.ServerProto.scalaDescriptor.services(0)
    def bindService(serviceImpl: Greeter, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
      _root_.io.grpc.ServerServiceDefinition.builder(SERVICE)
      .addMethod(
        METHOD_DISPLAY_TIME,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.cs441.anand.server.TimeRequest, com.cs441.anand.server.TimeReply] {
          override def invoke(request: com.cs441.anand.server.TimeRequest, observer: _root_.io.grpc.stub.StreamObserver[com.cs441.anand.server.TimeReply]): Unit =
            serviceImpl.displayTime(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .build()
  }
  
  /** The greeting service definition.
    */
  trait GreeterBlockingClient {
    def serviceCompanion = Greeter
    /** Sends a greeting
      */
    def displayTime(request: com.cs441.anand.server.TimeRequest): com.cs441.anand.server.TimeReply
  }
  
  class GreeterBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[GreeterBlockingStub](channel, options) with GreeterBlockingClient {
    /** Sends a greeting
      */
    override def displayTime(request: com.cs441.anand.server.TimeRequest): com.cs441.anand.server.TimeReply = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_DISPLAY_TIME, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): GreeterBlockingStub = new GreeterBlockingStub(channel, options)
  }
  
  class GreeterStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[GreeterStub](channel, options) with Greeter {
    /** Sends a greeting
      */
    override def displayTime(request: com.cs441.anand.server.TimeRequest): scala.concurrent.Future[com.cs441.anand.server.TimeReply] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_DISPLAY_TIME, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): GreeterStub = new GreeterStub(channel, options)
  }
  
  def bindService(serviceImpl: Greeter, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition = Greeter.bindService(serviceImpl, executionContext)
  
  def blockingStub(channel: _root_.io.grpc.Channel): GreeterBlockingStub = new GreeterBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): GreeterStub = new GreeterStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.cs441.anand.server.ServerProto.javaDescriptor.getServices().get(0)
  
}