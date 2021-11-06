package com.cs441.anand
import com.cs441.anand.server.{GreeterGrpc, TimeReply, TimeRequest}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GrpcServerTestSuite extends AnyFlatSpec with Matchers {
  it should "check if the protobuf's TimeRequest object is correct or not" in {
    val timeRequest = TimeRequest("00:00:00.000")
    assert(timeRequest.time == "00:00:00.000")
  }

  it should "check if the protobuf's TimeReply object is correct or not" in {
    val timeReply = TimeReply(false)
    assert(timeReply.message == false)
  }
}
