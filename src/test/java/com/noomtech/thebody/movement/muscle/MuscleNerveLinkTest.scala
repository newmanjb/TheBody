package com.noomtech.thebody.movement.muscle

import com.noomtech.thebody.IntegerPercentage
import com.noomtech.thebody.buildingblocks.nerve.Nerve
import com.noomtech.thebody.buildingblocks.nerve.Nerve.NerveImpulseGenerator
import com.noomtech.thebody.buildingblocks.transport.{Pipe, SingleConnectorPipe}
import com.noomtech.thebody.cells.RedBloodCell
import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers


class MuscleNerveLinkTest extends FlatSpec with MustMatchers with NerveImpulseGenerator {


  var muscle1 : Muscle = null
  var muscle2 : Muscle = null
  var muscle1Contracted : Boolean = false
  var muscle2Contracted : Boolean = false

  "A nerve impulse generator" should "send impulses to all registered nerve impulse receivers" in {

    //Give the muscles some oxygen (50 each) and check that this has gone through
    val pipe : Pipe = new SingleConnectorPipe("Test", null)
    muscle1 = new Muscle(pipe, IntegerPercentage(50), "Nerve Test 1")
    muscle2 = new Muscle(pipe, IntegerPercentage(50), "Nerve Test 2")
    val redBloodCell = new RedBloodCell("Test")
    redBloodCell.addO2(100);
    pipe.submit(redBloodCell)
    Thread.sleep(200)
    muscle1.getOxygenLevel must be (50)
    muscle2.getOxygenLevel must be (50)

    //This shouldn't make any difference
    sendImpulseToAll()
    muscle1.getOxygenLevel must be (50)
    muscle2.getOxygenLevel must be (50)

    //Link one to this nerve generator and test, then link the other and test.  If they have contracted they will have
    //used oxygen

    val forwardMuscleNerveName : String = "forward"
    val backMuscleNerveName : String = "back"
    Nerve.linkByNerve(forwardMuscleNerveName, muscle1.onContractionImpulse, this)
    sendImpulseToAll()
    muscle1.getOxygenLevel must be (49.5)
    muscle2.getOxygenLevel must be (50)
    sendImpulseTo(forwardMuscleNerveName)
    muscle1.getOxygenLevel must be (49.0)
    muscle2.getOxygenLevel must be (50)

    Nerve.linkByNerve(backMuscleNerveName, muscle2.onContractionImpulse, this)
    sendImpulseTo(backMuscleNerveName)
    muscle1.getOxygenLevel must be (49.0)
    muscle2.getOxygenLevel must be (49.5)
    sendImpulseToAll()
    muscle1.getOxygenLevel must be (48.5)
    muscle2.getOxygenLevel must be (49.0)
  }

  def contractedListener(muscle : Muscle): Unit = {
      if(muscle == muscle1) {
        muscle1Contracted = true
      }
      else {
        muscle2Contracted = true
      }
  }
}
