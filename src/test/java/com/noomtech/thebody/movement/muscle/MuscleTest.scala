package com.noomtech.thebody.movement.muscle

import com.noomtech.thebody.IntegerPercentage.IntegerPercent
import com.noomtech.thebody.buildingblocks.transport.SingleConnectorPipe
import com.noomtech.thebody.cells.RedBloodCell
import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers

import scala.math.BigDecimal
;


/**
 TEST-PLAN
 1 - Create a muscle and a pipe to deliver O2 to it and make sure that O2 levels are as they should be
 2 - contract from zero to 1%
 3 - zero to 10%
 4 - zero to 100%
 5 - a series of contractions that push the muscle to beyond its oxygen limits, so the muscle only contracts
     as far as it can, using the correct amount of oxygen in relation to these contraction levels
 6 - Should not respond to impulses that would result in the contraction level exceeding 100
  **/
class MuscleTest extends FlatSpec with MustMatchers {

  //1
  "A Muscle" should "consume oxygen correctly in direct relation to its size" in {
    val oxygenDeliverer = new SingleConnectorPipe("Oxygen Deliverer", null)
    val muscle = new Muscle(oxygenDeliverer, IntegerPercent(35), "Oxygen Consumer");

    muscle.getOxygenLevel must be (0)

    var redBloodCell = new RedBloodCell("Oxygen carrier")
    redBloodCell.addO2(50)
    oxygenDeliverer.submit(redBloodCell)

    Thread.sleep(100)

    muscle.getOxygenLevel must be (35)
    redBloodCell.getO2Level must be (15)

    oxygenDeliverer.submit(redBloodCell)

    Thread.sleep(500)

    muscle.getOxygenLevel must be (50)
    redBloodCell.getO2Level must be (0)

    oxygenDeliverer.submit(redBloodCell)

    Thread.sleep(500)

    muscle.getOxygenLevel must be (50)
    redBloodCell.getO2Level must be (0)
  }

  //2
  "A Muscle" should "be able to contract from 0% to 1% and use the correct amount of oxygen" in {

    var muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");

    sendImpulse(muscle, 1)
    
    muscle.getOxygenLevel must be (99.5)
  }

  //3
  "A Muscle" should "be able to contract from 0% to 10% and use the correct amount of oxygen" in {

    var muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");

    sendImpulse(muscle, 10)

    muscle.getOxygenLevel must be (95)
  }

  //4
  "A Muscle" should "be able to contract from 0% to 100% and use the correct amount of oxygen" in {

    var muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");
    val endTime = sendImpulse(muscle, 100)

    muscle.getOxygenLevel must be (50)
  }

  //5
  "A muscle" should "only be able to contract as far as its oxygen levels will allow it" in {
    var muscle = new Muscle(IntegerPercent(50), 1, 0, "Muscle test");

    sendImpulse(muscle, 3)

    muscle.getOxygenLevel must be (0)
    muscle.currentContractionLevel.get() must be (2)

    muscle = new Muscle(IntegerPercent(40), 1, 0, "Muscle test");

    sendImpulse(muscle, 3)

    muscle.getOxygenLevel must be (0.2)
    muscle.currentContractionLevel.get() must be (2)

    muscle.oxygenStored = BigDecimal.double2bigDecimal(8.3)

    sendImpulse(muscle, 8);

    muscle.currentContractionLevel.get() must be (10)
    muscle.getOxygenLevel must be (5.1)

    sendImpulse(muscle, 13)

    muscle.currentContractionLevel.get() must be (22)
    muscle.getOxygenLevel must be (0.3)
  }

  //6
  "A muscle" should "not respond to impulses that would result in the contraction level exceeding 100" in {
    var muscle = new Muscle(IntegerPercent(100), 100, 98, "Muscle test");
    sendImpulse(muscle, 3)

    muscle.currentContractionLevel.get() must be (100)
    muscle.getOxygenLevel must be (98)
  }

  private def sendImpulse(muscle : Muscle, numberOfImpulses : Int): Unit = {
    for(i <- 1 to numberOfImpulses) {
      muscle.onContractionImpulse()
    }
  }
}
