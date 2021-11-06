package com.cs441.anand
import com.cs441.anand.Utils.{CreateLogger, ObtainConfigReference}

class Driver

/**
 * Factory for [[Driver]] instances
 * The workflow starts here
 * */
object Driver {
  val logger = CreateLogger(this.getClass)

  /** Main Method - Triggers the gRPC or REST server based on command line input
   * @param args : Array[String] - command line input
   * @return Unit
   */
  def main(args: Array[String]): Unit = {
    val config = ObtainConfigReference("Client") match {
      case Some(value) => value
      case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
    }

    // switch which MapReduce to start based on args(0)
    // "1" - Invoke gRPC server's lambda function
    // "2" - Invoke REST server's lambda function
    args(0) match {
      case "1" => runGrpc("https://byjm817d36.execute-api.us-east-1.amazonaws.com/prod/loggrpc")
      case "2" => try
      {
        runRest("https://ebppmwnp6d.execute-api.us-east-1.amazonaws.com/prod/logrest", args(1))
      } catch {
        case ex : java.lang.ArrayIndexOutOfBoundsException => {
          runRest("https://ebppmwnp6d.execute-api.us-east-1.amazonaws.com/prod/logrest")
          //runRest("https://j621bpobs7.execute-api.us-east-1.amazonaws.com/test/test")
        }
      }
    }

    /** Method that invokes gRPC lambda
     * @param url : String - The endpoint for the gRPC lambda
     * @return Unit
     */
    def runGrpc(url: String) = {
      // Get the parameter from config
      val time = config.getString("Client.logTime")

      // Create a GrpcClient client object with the endpoint
      val client = new GrpcClient(url)

      // Invoke the lambda function and get the result
      val result = client.invokeLambda(time)
      logger.info("The time " + time + " exists in the log file: " + result + "\n")
    }

    /** Method that invokes REST lambda
     * @param url : String - The endpoint for the REST lambda
     * @return Unit
     */
    def runRest(url: String, md5: String = "null") = {
      // Get the parameters from the config file
      val logTime = config.getString("Client.logTime")
      val dT = config.getString("Client.dT")
      val pattern = config.getString("Client.pattern")

      // Create a RestClient object with the endpoint
      val client = new RestClient(url)

      // Invoke the lambda function and get the result
      val result = client.invokeLambda(logTime, dT, pattern, md5)
      logger.info("JSON response received from the server: " + "\n" + result + "\n")
    }
  }
}
