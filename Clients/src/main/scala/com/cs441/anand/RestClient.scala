package com.cs441.anand

import com.cs441.anand.Utils.CreateLogger
import com.google.gson.Gson
import scalaj.http.Http

import java.util

/**
 * Class defining [[RestClient]] instances
 * Constructor requires parameter - url
 * */
class RestClient(private var url: String) {

  val logger = CreateLogger(this.getClass)

  // Create a gson object to serialize the payload
  val gson =  new Gson()

  // POJO-like classes for Payload and Response
  case class Payload(logTime: String, dT: String, pattern: String, md5: String)
  case class Response(logs: util.Map[String, List[String]])

  /** Method that invokes the REST server lambda
   * @param logTime : String - The time parameter from the config
   * @param dT : String - The deltaT parameter from the config
   * @param pattern : String - The regex pattern from the config
   * @return : String - JSON String response, which can be parsed as an Object
   */
  def invokeLambda(logTime: String, dT: String, pattern: String, md5: String): String = {
    // Parse the Payload as a JSON string
    val payload = Payload(logTime, dT, pattern, md5)
    val payloadJson = gson.toJson(payload)
    logger.info("Converting payload to JSON success: " + payloadJson + "\n")

    // Invoke the lambda with the JSON payload
    val request = Http(url).headers(
      Map(
        "'Content-Type'" -> "'application/json'"
      )
    ).postData(payloadJson.getBytes())
      .timeout(5000, 12500)

    // Get the response body
    val response = request.asString.body

    // Process the response body
    val body = response.replaceAll("\\\\", "").replace("\"\"", "")
    //logger.info("Received HTTP response body: " + body + "\n")

    // Return the response body
    return body
  }
}
