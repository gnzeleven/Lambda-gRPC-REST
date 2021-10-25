package com.cs441.anand

import com.cs441.anand.Utils.CreateLogger

import java.util.logging.Logger
import io.grpc.{Server, ServerBuilder}
import com.cs441.anand.server.{GreeterGrpc, HelloReply, HelloRequest}

import scala.concurrent.{ExecutionContext, Future}

object GrpcServer {

  def main(args: Array[String]): Unit = {
    val server = new GrpcServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }

  private val port = 50051
}

class GrpcServer(executionContext: ExecutionContext) { self =>
  private[this] var server: Server = null
  val logger = CreateLogger(classOf[GrpcServer.type])

  private def start(): Unit = {
    server = ServerBuilder.forPort(GrpcServer.port).addService(GreeterGrpc.bindService(new GreeterImpl, executionContext)).build.start
    logger.info("Server started, listening on " + GrpcServer.port)
    sys.addShutdownHook {
      logger.info("*** shutting down gRPC server since JVM is shutting down")
      self.stop()
      logger.info("*** server shut down")
    }
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  private class GreeterImpl extends GreeterGrpc.Greeter {
    override def sayHello(req: HelloRequest) = {
      val reply = HelloReply(message = "Hello " + req.name)
      Future.successful(reply)
    }
  }
}


