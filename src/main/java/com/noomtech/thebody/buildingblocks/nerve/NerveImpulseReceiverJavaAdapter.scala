package com.noomtech.thebody.buildingblocks.nerve

/**
 */
class NerveImpulseReceiverJavaAdapter(nerveImpulseReceiverJava : NerveImpulseReceiverForJava) {


  def onNerveImpulse() : Unit = {
    nerveImpulseReceiverJava.onNerveImpulse()
  }
}
