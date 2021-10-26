package com.cs441.anand

import com.cs441.anand.Utils.{CreateLogger, ObtainConfigReference}
import com.cs441.anand.client.TimeRequest

class Driver

object Driver {
  val logger = CreateLogger(this.getClass)
  def main(args: Array[String]): Unit = {
    val config = ObtainConfigReference("GrpcClient") match {
      case Some(value) => value
      case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
    }
    val time = config.getString("GrpcClient.logTime")
    val url = " https://byjm817d36.execute-api.us-east-1.amazonaws.com/prod/loggrpc"
    val client = new GrpcClient(url)
    logger.info("**********Result********** " + time + "\n")
    val result = client.displayTime(TimeRequest(time))
    logger.info("**********Result********** " + result + "\n")
  }
}
