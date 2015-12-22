package com.noomtech.thebody.movement.muscle

/**
 * Binds two muscles together as a pair (a and b) who's movements oppose each other e.g. biceps and triceps, and handles the
 * detraction of one during the contraction of the other.
 */
class MusclePair(a : Muscle, b : Muscle) {


  private val contractedListener = (muscle : Muscle) => {
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
}