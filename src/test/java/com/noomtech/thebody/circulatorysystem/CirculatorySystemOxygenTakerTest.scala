package com.noomtech.thebody.circulatorysystem

import java.util.concurrent.atomic.AtomicInteger

import com.noomtech.thebody.Oxygen
import com.noomtech.thebody.buildingblocks.transport.{Particle, ParticleProcessor, Pipe}
import com.noomtech.thebody.cells.RedBloodCell
import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers


class CirculatorySystemOxygenTakerTest extends FlatSpec with MustMatchers {


  "CirculatorySystemOxygenTaker" must "take the correct amount of oxygen from RedBloodCell instances" in {

    val totalOxygen : AtomicInteger = new AtomicInteger(0)
    def oxygenReceiver(oxygen : Oxygen) : Unit = {
      totalOxygen.addAndGet(oxygen.amount)
    }
    val testPipe : TestPipe = new TestPipe
    var circulatorySystemOxygenTaker : CirculatorySystemOxygenTaker =
      new CirculatorySystemOxygenTaker(11, oxygenReceiver)
    testPipe.addParticleProcessor(circulatorySystemOxygenTaker)

    totalOxygen.get() must equal (35)

    circulatorySystemOxygenTaker = new CirculatorySystemOxygenTaker(40, oxygenReceiver)
    totalOxygen.set(0)
    testPipe.addParticleProcessor(circulatorySystemOxygenTaker)

    totalOxygen.get() must equal (41)
  }


  private class TestPipe extends Pipe {

    override def addParticleProcessor(particleProcessor:ParticleProcessor) = {
      val rbc1 : RedBloodCell = new RedBloodCell("rbc1")
      val rbc2 : RedBloodCell = new RedBloodCell("rbc2")
      val rbc3 : RedBloodCell = new RedBloodCell("rbc3")
      val rbc4 : RedBloodCell = new RedBloodCell("rbc3")
      val particle1 : Particle = new Particle("p1");
      val particle2 : Particle = new Particle("p2");
      val particle3 : Particle = new Particle("p3");
      val particle4 : Particle = new Particle("p4");

      rbc1.addO2(5)
      rbc2.addO2(8)
      rbc3.addO2(11)
      rbc4.addO2(17)

      val particleList : List[Particle] = rbc1::particle1::rbc2::particle2::rbc3::particle3::rbc4::particle4::Nil

      for(particle : Particle <- particleList) {
        particleProcessor.processParticle(particle)
      }
    }

    def passToForwardConnection(p: Particle): Boolean = {false}

    def passOnShockWaveToConnections(force: Int, source: AnyRef) = {}

    def getForwardConnection1: Pipe = {null}
  }
}
