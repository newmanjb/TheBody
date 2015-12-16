package com.noomtech.thebody.movement.muscle

import com.noomtech.thebody.IntegerPercentage.IntegerPercent
import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers


class MusclePairTest extends FlatSpec with MustMatchers {


  "A muscle pair" should "keep contractions and detractions in sync between the two muscles" in {
    val forward = new Muscle(IntegerPercent(50), 500, 20, "Muscle pair test");
    val back = new Muscle(IntegerPercent(50), 500, 20, "Muscle pair test");
    val forwardAndBackMusclePair : ForwardAndBackMusclePair = new ForwardAndBackMusclePair(forward, back)

    forwardAndBackMusclePair.moveForward(IntegerPercent(1), 100);
    Thread.sleep(50)
    forward.contractionLevel must be (1)
    back.contractionLevel must be (99)
    forwardAndBackMusclePair.moveForward(IntegerPercent(2), 100);
    Thread.sleep(50)
    forward.contractionLevel must be (2)
    back.contractionLevel must be (98)
    forwardAndBackMusclePair.moveForward(IntegerPercent(100), 1000);
    Thread.sleep(250)
    forward.contractionLevel must be (100)
    back.contractionLevel must be (0)

    forwardAndBackMusclePair.moveBack(IntegerPercent(1), 100);
    Thread.sleep(50)
    forward.contractionLevel must be (99)
    back.contractionLevel must be (1)
    forwardAndBackMusclePair.moveBack(IntegerPercent(2), 100);
    Thread.sleep(50)
    forward.contractionLevel must be (98)
    back.contractionLevel must be (2)
    forwardAndBackMusclePair.moveBack(IntegerPercent(100), 1000);
    Thread.sleep(250)
    forward.contractionLevel must be (0)
    back.contractionLevel must be (100)
  }
}
