package com.noomtech.thebody.movement.muscle

import java.util.concurrent.atomic.AtomicInteger

import com.noomtech.thebody.IntegerPercentage.IntegerPercent
import com.noomtech.thebody.Oxygen
import com.noomtech.thebody.buildingblocks.Named
import com.noomtech.thebody.buildingblocks.nerve.Nerve.NerveImpulseReceiver
import com.noomtech.thebody.buildingblocks.transport.Pipe
import com.noomtech.thebody.circulatorysystem.CirculatorySystemOxygenTaker

import scala.math._


/**
 * Represents a muscle in the body.  It has a supply of oxygen and can be told to contract by nerve impulses.
 * It will contract by 1% for every never impulse received.
 * Each muscle has a size that is between 1 and 100%.  The size is relative, so 100% is the size of the largest muscles
 * in the body and 1% is the smallest.
 * For each contraction it will use an amount of its oxygen supply hat is directly proportional to the muscle's size.
 *
 * @constructor
 * @param pipe The pipe in the circulatory system that this muscle will gets its oxygen supply from
 * @param relativeSize A relative value where 100% is the size of the largest muscle in the body.  This is used to determine
 * properties such as the amount of oxygen the muscle needs to move and its strength
 */
class Muscle(pipe : Pipe, relativeSize : IntegerPercent, name : String) extends Named {

  private[muscle] def this(relativeSize: IntegerPercent, initialOxygen : Int, initalContractionLevel : Int, name: String) {
    this(null, relativeSize, name)
    oxygenStored += initialOxygen
    currentContractionLevel.set(initalContractionLevel)
  }

  private[muscle] var oxygenStored : BigDecimal = 0
  def getOxygenLevel = oxygenStored.doubleValue()
  private val mutex = new Object()
  private val oxygenUsedPerUnitOfContraction : BigDecimal = relativeSize.intVal().asInstanceOf[Double]/100

  //Receives the oxygen needed for this muscle from the circulatory system
  def processNewOxygen(oxygen: Oxygen) = {
    mutex.synchronized {
      oxygenStored = oxygenStored + oxygen.amount
    }
  }

  //CirculatorySystemOxygenTaker will take the oxygen for the muscle
  private def oxygenTaker: CirculatorySystemOxygenTaker =
    new CirculatorySystemOxygenTaker(relativeSize.intVal(), processNewOxygen)

  //Add the CirculatorySystemOxygenTaker to the pipe provided
  if (pipe != null) {
    pipe.addParticleProcessor(oxygenTaker)
  }

  override def getName() : String = {name}

  private[muscle] val currentContractionLevel : AtomicInteger  = new AtomicInteger(0)

  private[muscle] var contractedListener : (Muscle) => Unit = null

  val onContractionImpulse : NerveImpulseReceiver = () => {

    mutex.synchronized {
      if (currentContractionLevel.get() < 100) {

        var doContraction: Boolean = false
        if (oxygenStored >= oxygenUsedPerUnitOfContraction) {
          oxygenStored = oxygenStored - oxygenUsedPerUnitOfContraction
          doContraction = true
        }
        if (doContraction) {
          currentContractionLevel.incrementAndGet()
          if(contractedListener != null) {
            contractedListener(this)
          }
        }
      }
    }
  }

  /**
   * Used when this muscle is part of a group in order to satisfy the laws of physics, so when the opposing
   * muscle contracts this one will be detracted with the use of this method
   */
  private[muscle] def detract() : Unit = {
    mutex.synchronized {
      currentContractionLevel.decrementAndGet()
    }
  }
}