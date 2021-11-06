package com.cs441.anand
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GrpcClientTestSuite extends AnyFlatSpec with Matchers {
  it should "check if the response from the lambda server is of type Boolean" in {
    val url = "https://byjm817d36.execute-api.us-east-1.amazonaws.com/prod/loggrpc"
    val client = new GrpcClient(url)
    val result = client.invokeLambda("00:00:00.000")
    assert(result.isInstanceOf[Boolean])
  }
}
