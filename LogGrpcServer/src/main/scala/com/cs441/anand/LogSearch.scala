package com.cs441.anand

object LogSearch {
    /* O(n) implementation of search for the timestamp */
}

/*
package com.cs441.anand

import com.amazonaws.AmazonServiceException
import com.amazonaws.SdkClientException
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.fasterxml.jackson.core.{JsonFactory, JsonParser}
import com.fasterxml.jackson.databind.ObjectMapper

import java.io.{BufferedReader, File, IOException, InputStream, InputStreamReader}
import scala.util.matching.Regex


object LogSearch {
//  val h = find("01:12:00.119")
//  print(h + "\n")
  def find(time: String): Boolean = {
    val clientRegion = "us-east-1"
    val bucketName = "logawsbucket"
    val key = "log/LogFileGenerator.2021-10-31.log"

    try {
      val s3Client = AmazonS3ClientBuilder
                    .standard
                    .withRegion(clientRegion)
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .build

      val fullObject = s3Client.getObject(new GetObjectRequest(bucketName, key))

      val s3Stream : S3ObjectInputStream = fullObject.getObjectContent
      val objectMapper : ObjectMapper = new ObjectMapper()
      val jsonFactory : JsonFactory = objectMapper.getFactory

      val isPresent = isTimePresent(fullObject.getObjectContent, time)
      fullObject.close
      return isPresent
    } catch {
      case e: AmazonServiceException => {
        e.printStackTrace
        return false
      }
      case e: SdkClientException => {
        e.printStackTrace
        return false
      }
    }
  }

  @throws(classOf[IOException])
  def isTimePresent(input: InputStream, time: String): Boolean = {
    val timePattern = new Regex("(\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{3})")
    val reader: BufferedReader = new BufferedReader(new InputStreamReader(input))
    while (reader.readLine() != null) {
      val line = reader.readLine()
      if (line != null && line.contains(time)) {
        return true
      }
    }
    return false
  }
}
*/