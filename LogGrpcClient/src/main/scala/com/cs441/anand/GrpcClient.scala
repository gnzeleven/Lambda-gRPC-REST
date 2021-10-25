package com.cs441.anand

import com.cs441.anand.Utils.CreateLogger

import java.util.concurrent.TimeUnit
import com.cs441.anand.client.GreeterGrpc.GreeterBlockingStub
import com.cs441.anand.client.{GreeterGrpc, HelloRequest}
import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}

object GrpcClient {
  def apply(host: String, port: Int): GrpcClient = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build
    val blockingStub = GreeterGrpc.blockingStub(channel)
    new GrpcClient(channel, blockingStub)
  }

  def main(args: Array[String]): Unit = {
    val client = GrpcClient("localhost", 50051)
    try {
      val user = args.headOption.getOrElse("world")
      client.greet(user)
    } finally {
      client.shutdown()
    }
  }
}

class GrpcClient private(private val channel: ManagedChannel, private val blockingStub: GreeterBlockingStub) {
  //create a logger for this class
  val logger = CreateLogger(classOf[GrpcClient.type])

  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  /** Say hello to server. */
  def greet(name: String): Unit = {
    logger.info("Will try to greet " + name + " ...")
    val request = HelloRequest(name = name)
    try {
      val response = blockingStub.sayHello(request)
      logger.info("Greeting: " + response.message)
    }
    catch {
      case e: StatusRuntimeException =>
        logger.warn("RPC failed: {0}", e.getStatus)
    }
  }
}
