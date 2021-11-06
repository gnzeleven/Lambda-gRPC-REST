package com.cs441.anand
import com.cs441.anand.Utils.ObtainConfigReference
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DriverTestSuite extends AnyFlatSpec with Matchers {
  val config = ObtainConfigReference("Client") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  it should "check if the application.conf parameters work" in {
    val logTime = config.getString("Client.logTime")
    val dT = config.getString("Client.dT")
    val pattern = config.getString("Client.pattern")
    assert(!(logTime.isEmpty && dT.isEmpty && pattern.isEmpty))
  }
}
