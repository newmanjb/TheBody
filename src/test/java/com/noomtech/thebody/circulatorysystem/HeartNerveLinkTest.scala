package com.noomtech.thebody.circulatorysystem

import com.noomtech.thebody.buildingblocks.nerve.Nerve
import com.noomtech.thebody.buildingblocks.nerve.Nerve.NerveImpulseGenerator
import com.noomtech.thebody.buildingblocks.transport.{Particle, Pipe}
import com.noomtech.thebody.communication.{CommunicatorEventListener, Communicator, AbstractCommunicator, ConsoleCommunicator}
import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers

/**
 */
class HeartNerveLinkTest extends FlatSpec with MustMatchers with NerveImpulseGenerator with CommunicatorEventListener {

  var numOfBeats : Int = 0

  "A heart" should "beat at the exact rate determined by the nerve impulses" in {
    val testAtrium : Atrium = new Atrium("Test Atrium", null)
    val testHeart : Heart = new Heart(testAtrium, 0)

    val nerveName : String = "testHeartBeat"
    Nerve.linkByNerve(nerveName, testHeart.getNerveImpulseReceiverJavaAdapter.onNerveImpulse, this)
    val communicator : Communicator = new TestCommunicator();
    communicator.addEventListener(this, Heart.HEART_BEAT_EVENT)

    sendImpulseTo(nerveName)
    Thread.sleep(10)
    sendImpulseTo(nerveName)
    Thread.sleep(10)
    sendImpulseTo(nerveName)
    Thread.sleep(10)
    sendImpulseTo(nerveName)
    Thread.sleep(10)
    sendImpulseTo(nerveName)
    Thread.sleep(10)
    sendImpulseTo(nerveName)
    Thread.sleep(10)

    numOfBeats must be (6)
  }

  override def onEvent(subject : String, message : String) {
    numOfBeats = numOfBeats + 1
  }

  private class TestCommunicator extends AbstractCommunicator {
      override def postExceptionEvent(message : String, exception : Exception) : Unit = {}
  }
}
