
package com.cs441.anand

object LogSearch {
  /* O(n) implementation of search for the range */
}

/*
import com.amazonaws.AmazonServiceException
import com.amazonaws.SdkClientException
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.GetObjectRequest

import scala.collection.mutable.ListBuffer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalTime
import java.util
import scala.collection.JavaConverters._
import scala.util.matching.Regex

object LogSearch extends {
  def getLogs(event: util.Map[String, String]): List[String] = {
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

      val affirmative = isTimePresent(fullObject.getObjectContent, event)
      fullObject.close
      return affirmative
    } catch {
      case e: AmazonServiceException => {
        e.printStackTrace
        return List()
      }
      case e: SdkClientException => {
        e.printStackTrace
        return List()
      }
    }
  }

  @throws(classOf[IOException])
  def isTimePresent(input: InputStream, event: util.Map[String, String]): List[String] = {
    val time = LocalTime.parse(event.get("logTime"))
    val dT  = LocalTime.parse(event.get("dT"))
    val stringPattern: Regex = event.get("pattern").r

    val before  = time.minusHours(dT.getHour()).minusMinutes(dT.getMinute()).minusSeconds(dT.getSecond()).minusNanos(dT.getNano());
    val after  = time.plusHours(dT.getHour()).plusMinutes(dT.getMinute()).plusSeconds(dT.getSecond()).plusNanos(dT.getNano());

    val timePattern = new Regex("(\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{3})")

    val affirmative = new ListBuffer[String]
    val reader: BufferedReader = new BufferedReader(new InputStreamReader(input))
    while (reader.readLine() != null) {
      val line = reader.readLine()
      if (line != null) {
        val split = line.split(" ")
        val timeFromLog = LocalTime.parse(split(0))
        val isPatternPresent = stringPattern.findFirstMatchIn(split.last) match {
          case Some(_) => true
          case None => false
        }

        if (before.isBefore(timeFromLog) && after.isAfter(timeFromLog)) {
            affirmative += line
        }
      }
    }
    return affirmative.toList
  }
}
*/