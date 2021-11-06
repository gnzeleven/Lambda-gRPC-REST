package com.cs441.anand

import com.cs441.anand.Utils.CreateLogger
import com.cs441.anand.client.{GreeterGrpc, TimeReply, TimeRequest}
import scalaj.http.Http

/**
 * Class defining [[GrpcClient]] instances
 * Constructor requires parameter - url
 * Extends GreeterGrpc.GreeterBlockingClient
 * */
class GrpcClient(private var url: String) extends GreeterGrpc.GreeterBlockingClient {

  val logger = CreateLogger(this.getClass)

  /** Override displayTime method from GreeterGrpc.GreeterBlockingClient
   * @param timeRequest : TimeRequest - The input time parameter as a TimeRequest object
   * @return : TimeReply - The boolean response as a TimeReply object
   */
  override def displayTime(timeRequest: TimeRequest): TimeReply = {
    // Invoke gRPC lambda function with the protobuf
    val request = Http(url).headers(Map(
      "Content-Type" -> "application/grpc+proto",
      "Accept" -> "application/grpc+proto"
    )).postData(timeRequest.toByteArray)
      .timeout(5000, 12500)

    logger.info("Sending HTTP request: " + request + "\n")

    // Get the response as Byte Array
    val response = request.asBytes

    // Parse the response to a TimeReply object
    val output = TimeReply.parseFrom(response.body)

    logger.info("Response from the server: " + output.message + "\n")

    // Return the TimeReply object
    return output
  }

  /** Method that invokes the gRPC server lambda
   * @param logTime : String - The time parameter from the config
   * @return : Boolean - Indicating whether the time is present in the logfile or not
   */
  def invokeLambda(logTime : String): Boolean = {
    // Call the overridden method with TimeRequest(logTime) as argument
    val output : TimeReply = displayTime(TimeRequest(logTime))

    // Return the Boolean
    return output.message
  }
}
