package com.cs441.anand

import com.cs441.anand.Utils.CreateLogger

import com.cs441.anand.server.{GreeterGrpc, TimeReply, TimeRequest}

import scala.concurrent.{Await, Future}
import scala.collection.JavaConverters._
import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

import scala.concurrent.duration.DurationInt
import java.util.Base64
import scala.language.postfixOps

class GrpcServer extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
{
  val logger = CreateLogger(this.getClass)
  override def handleRequest(request: APIGatewayProxyRequestEvent, context: Context) : APIGatewayProxyResponseEvent = {

    val message = if (request.getIsBase64Encoded) Base64.getDecoder.decode(request.getBody.getBytes) else request.getBody.getBytes
    logger.info("***********" + message.mkString(",") + "\n")

    val logTime = TimeRequest.parseFrom(message)
    val greeterService = new GreeterImpl()
    val response = Await.result(greeterService.displayTime(logTime), atMost = 10 seconds)
    val output = Base64.getEncoder.encodeToString(response.toByteArray)
    logger.info("***********" + output + "\n")

    new APIGatewayProxyResponseEvent()
      .withStatusCode(200)
      .withHeaders(Map("Content-Type" -> "application/grpc+proto").asJava)
      .withIsBase64Encoded(true)
      .withBody(output)
  }

  private class GreeterImpl extends GreeterGrpc.Greeter {
    override def displayTime(req: TimeRequest) = {
      val reply = TimeReply(message = "The time that you are looking for is " + req.time)
      Future.successful(reply)
    }
  }
}


