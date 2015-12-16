package com.noomtech.thebody.movement.muscle

import com.noomtech.thebody.IntegerPercentage.IntegerPercent

/**
 * A [[com.noomtech.thebody.movement.muscle.MusclePair]] that consists of muscles that do "forward" and "back"
 * movements
 */
class ForwardAndBackMusclePair(forward : Muscle, back : Muscle) extends MusclePair(forward, back) {


  def moveForward(requiredContractionLevel : IntegerPercent, contractionsPerSecond : Int): Unit = {
    super.startContractingA(requiredContractionLevel, contractionsPerSecond)
  }

  def moveBack(requiredContractionLevel : IntegerPercent, contractionsPerSecond : Int): Unit = {
    super.startContractingB(requiredContractionLevel, contractionsPerSecond)
  }

  def stopMovingForward(): Unit = {
    super.stopMovingA
  }

  def stopMovingBackward(): Unit = {
    super.stopMovingB
  }
}
