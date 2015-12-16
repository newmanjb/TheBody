package com.noomtech.thebody.utils


import com.noomtech.thebody.IntegerPercentage
import org.scalatest.FlatSpec
import org.scalatest.matchers.{MustMatchers}


class IntegerPercentTest extends FlatSpec with MustMatchers {


  "Creating a Percent with a value of < 0 or > 100" must "result in an IllegalArgumentException" in {
    var caughtCorrectException : Boolean = false
    try {
      IntegerPercentage(-1)
    }
    catch {
      case IntegerPercentage.PercentOutOfRangeException(s) => caughtCorrectException = true
    }

    caughtCorrectException must equal (true)

    caughtCorrectException = false
    try {
      IntegerPercentage(101)
    }
    catch {
      case IntegerPercentage.PercentOutOfRangeException(s) => caughtCorrectException = true
    }

    caughtCorrectException must equal (true)
  }

  "Two percents with different percent values" must "not be considered equals" in {
    val p1 = IntegerPercentage(4)
    val p2 = IntegerPercentage(5)
    p1.equals(p2) must equal (false)
    p1 == p2 must equal (false)
    p1 != p2 must equal (true)
  }

  "Two percents with the same percent value" must "be considered equals" in {
    val p3 = IntegerPercentage(6)
    val p4 = IntegerPercentage(6)
    p3.equals(p4) must equal (true)
    p3 == p4 must equal (true)
    p3 != p4 must equal (false)
  }

  "The * operator" must "work for Percent and a number" in {
    (IntegerPercentage(40) * 10) must equal (4)
  }

  "The + operation" must "work for two percent values" in {
    (IntegerPercentage(30) + IntegerPercentage(50)) must equal (IntegerPercentage(80))
  }

  "The - operation" must "work for two percent values" in {
    (IntegerPercentage(50) - IntegerPercentage(30)) must equal (IntegerPercentage(20))
  }

  "The > operation" must "work for two percent values" in {
    IntegerPercentage(20) > IntegerPercentage(6) must equal (true)
    IntegerPercentage(20) > IntegerPercentage(26) must equal (false)
  }

  "The < operation" must "work for two percent values" in {
    IntegerPercentage(20) < IntegerPercentage(26) must equal (true)
    IntegerPercentage(30) < IntegerPercentage(26) must equal (false)
  }

  "The >= operation" must "work for two percent vales" in {
    IntegerPercentage(20) >= IntegerPercentage(6) must equal (true)
    IntegerPercentage(20) >= IntegerPercentage(26) must equal (false)
    IntegerPercentage(20) >= IntegerPercentage(20) must equal (true)
  }

  "The <= operation" must "work for two percent values" in {
    IntegerPercentage(20) <= IntegerPercentage(26) must equal (true)
    IntegerPercentage(30) <= IntegerPercentage(26) must equal (false)
    IntegerPercentage(30) <= IntegerPercentage(30) must equal (true)
  }

  "The > operation" must "work for Percent and a number" in {
    IntegerPercentage(20) > 6 must equal (true)
    IntegerPercentage(20) > 26 must equal (false)
  }

  "The < operation" must "work for Percent and a number" in {
    IntegerPercentage(20) < 26 must equal (true)
    IntegerPercentage(30) < 26 must equal (false)
  }

  "The >= operation" must "work for Percent and a number" in {
    IntegerPercentage(20) >= 6 must equal (true)
    IntegerPercentage(20) >= 26 must equal (false)
    IntegerPercentage(20) >= 20 must equal (true)
  }

  "The <= operation" must "work for Percent and a number" in {
    IntegerPercentage(20) <= 26 must equal (true)
    IntegerPercentage(30) <= 26 must equal (false)
    IntegerPercentage(30) <= 30 must equal (true)
  }

  "The == operation" must "work for Percent and another Percent" in {
    IntegerPercentage(20) == IntegerPercentage(20) must equal (true)
    IntegerPercentage(20) == IntegerPercentage(21) must equal (false)
  }

  "The == operation" must "work for Percent and a number" in {
    IntegerPercentage(20) == 20 must equal (true)
    IntegerPercentage(20) == 21 must equal (false)
  }
}
