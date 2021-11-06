package com.cs441.anand
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.google.gson.Gson
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.JavaConverters._
import java.util

class RestServerTestSuite extends AnyFlatSpec with Matchers {

  case class Request(method: String, logTime: String, minus: String, plus: String, pattern: String, md5: String)
  val gson = new Gson()

  it should "check if the json string is created properly" in {
    val request = Request("getlogs", "04:00:00.000", "03:00:00.000", "05:00:00.000", "[0-9]+", "2")
    assert(gson.toJson(request).isInstanceOf[String])
  }

  it should "check if the APIGatewayProxyResponseEvent() works as intended" in {
    val body = Request("getlogs", "04:00:00.000", "03:00:00.000", "05:00:00.000", "[0-9]+", "2")
    val response = new APIGatewayProxyResponseEvent()
                      .withStatusCode(200)
                      .withHeaders(Map("Content-Type" -> "application/json").asJava)
                      .withBody(gson.toJson(body))
    assert(response.getStatusCode == 200
            && response.getHeaders() == Map("Content-Type" -> "application/json").asJava
            && response.getBody.isInstanceOf[String])
  }
}