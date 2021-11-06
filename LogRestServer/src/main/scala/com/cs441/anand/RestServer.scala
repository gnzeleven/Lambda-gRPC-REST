package com.cs441.anand

import scala.collection.JavaConverters._
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.google.gson.Gson
import scalaj.http.Http

import java.time.LocalTime
import java.util

/**
 * Class defining [[RestHandler]] instances
 * Extends RequestHandler[util.Map[String, String], APIGatewayProxyResponseEvent]
 * */
class RestHandler extends RequestHandler[util.Map[String, String], APIGatewayProxyResponseEvent]
{
  val gson = new Gson()

  /** Override handleRequest method
   * @param event : util.Map[String, String] - The payload coming from the client
   * @param context: Context object
   * @return : APIGatewayProxyResponseEvent
   */
  override def handleRequest(event: util.Map[String, String], context: Context) : APIGatewayProxyResponseEvent = {
    val logger : LambdaLogger = context.getLogger()

    // Basic health checks
    logger.log("CONTEXT: " + gson.toJson(context))
    logger.log("EVENT: " + gson.toJson(event))

    // Get the parameters from the event
    val time : LocalTime = LocalTime.parse(event.get("logTime"))
    val dt : LocalTime = LocalTime.parse(event.get("dT"))
    val pattern : String = event.get("pattern")
    val md5 = event.get("md5")

    // Compute the upper and lower time limits based on delta
    val minus : LocalTime = time.minusHours(dt.getHour()).minusMinutes(dt.getMinute()).minusSeconds(dt.getSecond()).minusNanos(dt.getNano())
    val plus : LocalTime = time.plusHours(dt.getHour()).plusMinutes(dt.getMinute()).plusSeconds(dt.getSecond()).plusNanos(dt.getNano())

    // Create a Request object - this will be converted to JSON string
    val request = Request("getlogs", time.toString, minus.toString, plus.toString, pattern, md5)

    // Send httpRequest to child lambda function with JSON payload
    val httpRequest = Http("https://0nplrbh3wk.execute-api.us-east-1.amazonaws.com/prod/child")
      .headers(Map(
        "'Content-Type'" -> "'application/json'"
      )).timeout(1000, 2500).postData(gson.toJson(request))

    // Get the response from child lambda
    val strResponse = httpRequest.asString.body

    // Set status code
    val statusCode = if (strResponse.length() < 20) 404 else 200

    // The Response object to respond to the client
    val response = Response(strResponse.substring(1, strResponse.length()-1), Map("Content-Type" -> "application/json"), statusCode)

    logger.log("EVENT TYPE: " + event.getClass)

    // Return APIGatewayProxyResponseEvent as response
    return new APIGatewayProxyResponseEvent()
      .withStatusCode(response.statusCode)
      .withHeaders(response.getHeaders)
      .withBody(gson.toJson(response.body))
  }

  // POJO-like classes for Request and Response
  case class Request(method: String, logTime: String, minus: String, plus: String, pattern: String, md5: String)
  case class Response(body: Object, headers: Map[String,String], statusCode: Int) {
    def getHeaders: java.util.Map[String, String] = headers.asJava
  }
}