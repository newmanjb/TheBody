package com.noomtech.thebody.movement.muscle

import com.noomtech.thebody.IntegerPercentage.IntegerPercent
import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers


class MusclePairTest extends FlatSpec with MustMatchers {


  "A muscle pair" should "keep contractions and detractions in sync between the two muscles" in {
    val forward = new Muscle(IntegerPercent(50), 500, 20, "Muscle pair test");
    val back = new Muscle(IntegerPercent(50), 500, 20, "Muscle pair test");
    val musclePair : MusclePair = new MusclePair(forward, back)

    sendImpulse(forward, 1);
    forward.currentContractionLevel.get() must be (1)
    back.currentContractionLevel.get() must be (99)

    sendImpulse(forward, 1);
    forward.currentContractionLevel.get() must be (2)
    back.currentContractionLevel.get() must be (98)

    sendImpulse(forward, 98);
    forward.currentContractionLevel.get() must be (100)
    back.currentContractionLevel.get() must be (0)

    sendImpulse(back, 1)
    forward.currentContractionLevel.get() must be (99)
    back.currentContractionLevel.get() must be (1)

    sendImpulse(back, 1);
    forward.currentContractionLevel.get() must be (98)
    back.currentContractionLevel.get() must be (2)

    sendImpulse(back, 98);
    forward.currentContractionLevel.get() must be (0)
    back.currentContractionLevel.get() must be (100)
  }

  private def sendImpulse(muscle : Muscle, numberOfImpulses : Int): Unit = {
    for(a <- 1 to numberOfImpulses) {
      muscle.onContractionImpulse()
    }
  }
}
