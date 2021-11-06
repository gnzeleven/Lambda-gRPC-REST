package com.cs441.anand

import com.cs441.anand.Utils.CreateLogger
import com.cs441.anand.server.{GreeterGrpc, TimeReply, TimeRequest}
import scalaj.http.Http

import scala.concurrent.{Await, Future}
import scala.collection.JavaConverters._
import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.google.gson.Gson

import scala.concurrent.duration.DurationInt
import java.util.Base64
import scala.language.postfixOps

/**
 * Class defining [[GrpcServer]] instances
 * Extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
 * */
class GrpcServer extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
{
  val logger = CreateLogger(this.getClass)

  /**
   * Class defining [[GreeterImpl]] instances
   * extends GreeterGrpc.Greeter
   * */
  private class GreeterImpl extends GreeterGrpc.Greeter {

    // POJO-like classes for Body
    case class Body(method: String, logTime: String)

    /** Override displayTime method from GreeterGrpc.GreeterBlockingClient
     * @param timeRequest : TimeRequest - The input time parameter as a TimeRequest object
     * @return : Future[TimeReply] - A Future with the TimeReply response
     */
    override def displayTime(timeRequest: TimeRequest) : Future[TimeReply] = {
      val time = Body("1", timeRequest.time)

      // Request to invoke lambda
      val request = Http("https://0nplrbh3wk.execute-api.us-east-1.amazonaws.com/prod/child")
        .headers(Map(
          "'Content-Type'" -> "'application/json'"
        )).timeout(2000, 4000).postData(new Gson().toJson(time))

      // Return the Future
      Future.successful(TimeReply(request.asString.body.toBoolean))
    }
  }

  // POJO-like classes for Response
  case class Response(headers: Map[String, String], body: String, isBase64Encoded: Boolean, statusCode: Int) {
    def getHeaders : java.util.Map[String, String] = headers.asJava
  }

  /** Override handleRequest method
   * @param event : util.Map[String, String] - The payload coming from the client
   * @param context: Context object
   * @return : APIGatewayProxyResponseEvent
   */
  override def handleRequest(event: APIGatewayProxyRequestEvent, context: Context) : APIGatewayProxyResponseEvent = {

    // Decode the Base64 encoded message bytes
    // API gateway is configured in such a way to accept Base64 encoded
    // data in the byte array format
    val message = Base64.getDecoder.decode(event.getBody.getBytes)

    // create a TimeRequest object from the message
    val logTime = TimeRequest.parseFrom(message)

    // Create a new GreeterImpl service object
    val greeterService = new GreeterImpl()

    // Wait for the response from the service
    val response = Await.result(greeterService.displayTime(logTime), atMost = 6 seconds)

    // Encode the response in Base64
    val output = Base64.getEncoder.encodeToString(response.toByteArray)

    // Create a Response object
    val responseObj = Response(Map("Content-Type" -> "application/grpc+proto"), output, true, 200)

    // Return APIGatewayProxyResponseEvent as response
    new APIGatewayProxyResponseEvent()
      .withHeaders(responseObj.getHeaders)
      .withBody(responseObj.body)
      .withIsBase64Encoded(responseObj.isBase64Encoded)
      .withStatusCode(responseObj.statusCode)
  }
}


