package com.cs441.anand

import com.cs441.anand.Utils.{CreateLogger}

import com.cs441.anand.client.{GreeterGrpc, TimeReply, TimeRequest}
import scalaj.http.Http

class GrpcClient(private var url: String) extends GreeterGrpc.GreeterBlockingClient {

  val logger = CreateLogger(this.getClass)
  override def displayTime(logTime: TimeRequest): TimeReply = {
    val request = Http(url).headers(Map(
      "Content-Type" -> "application/grpc+proto",
      "Accept" -> "application/grpc+proto"
    )).timeout(2000, 10000).postData(logTime.toByteArray)
    logger.info("Sending HTTP request: " + request + "\n")
    val response = request.asBytes
    logger.info("Received HTTP response1: " + response + "\n")
    logger.info("Received HTTP response2: " + response.body.mkString(",") + "\n")
    val output = TimeReply.parseFrom(response.body)
    logger.info("Response from the server: " + output.message + "\n")
    return output
  }
}
