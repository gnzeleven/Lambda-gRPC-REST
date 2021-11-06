package com.cs441.anand
import com.cs441.anand.Utils.ObtainConfigReference
import com.google.gson.Gson
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RestClientTestSuite extends AnyFlatSpec with Matchers {
  val gson =  new Gson()
  case class Payload(logTime: String, dT: String, pattern: String, md5: String)

  it should "check if the application.conf parameter are right" in {
    val payload = Payload("00:00:00.000", "01:00:00.000", "[0-9]+", "null")
    assert(gson.toJson(payload) == "{\"logTime\":\"00:00:00.000\",\"dT\":\"01:00:00.000\",\"pattern\":\"[0-9]+\",\"md5\":\"null\"}")
  }
}
