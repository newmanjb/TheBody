package com.noomtech.thebody.movement.muscle

import com.noomtech.thebody.IntegerPercentage.IntegerPercent

/**
 * Binds two muscles together as a pair (a and b) who's movements oppose each other e.g. biceps and triceps, and handles the
 * detraction of one during the contraction of the other.
 *
 * It also provides a facade through which the muscles can be controlled
 */
abstract class MusclePair(a : Muscle, b : Muscle) {


  private var contractedListener = (muscle : Muscle, newContractionLevel:Int) => {
    if(muscle == a) {
      b.detract()
    }
    else {
      a.detract()
    }
  }

  a.currentContractionLevel.set(0)
  b.currentContractionLevel.set(100)
  a.contractedListener = contractedListener
  b.contractedListener = contractedListener

  protected def startContractingA(requiredContractionLevel : IntegerPercent, contractionsPerSec : Int): Boolean = {
    a.startContracting(requiredContractionLevel, contractionsPerSec)
  }

  protected def startContractingB(requiredContractionLevel : IntegerPercent, contractionsPerSec : Int): Boolean = {
    b.startContracting(requiredContractionLevel, contractionsPerSec)
  }

  protected def stopMovingA : Unit = {
    a.stopContracting()
  }

  protected def stopMovingB : Unit = {
    b.stopContracting()
  }
}