package com.noomtech.thebody.movement.muscle

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}

import com.noomtech.thebody.IntegerPercentage.IntegerPercent
import com.noomtech.thebody.buildingblocks.{Named, Pipe}
import com.noomtech.thebody.circulatorysystem.CirculatorySystemOxygenTaker
import com.noomtech.thebody.{NerveImpulseReceiver, IntegerPercentage, Oxygen}

import scala.math._


/**
 * Represents a muscle in the body.  It has a supply of oxygen and can be told to contract to any degree, whereupon it
 * will use its oxygen supply to an extent that is directly proportional to the amount of movement and the muscle's
 * size.
 *
 * All members marked package-private are used for testing only.
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
  private val oxygenStoreMutex = new Object()
  private val oxygenUsedPerUnitOfContraction : BigDecimal = relativeSize.intVal().asInstanceOf[Double]/100

  //Receives the oxygen needed for this muscle from the circulatory system
  def processNewOxygen(oxygen: Oxygen) = {
    oxygenStoreMutex.synchronized {
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

  private[muscle] var startContractingListener : () => Unit = null
  private[muscle] var stopContractingListener : () => Unit = null
  private[muscle] var contractedListener : (Muscle,Int) => Unit = null

  private[muscle] val currentContractionLevel : AtomicInteger  = new AtomicInteger(0)
  private val contracting : AtomicBoolean = new AtomicBoolean(false)
  private val contractingMutex = new Object()

  private var contractorThread : Thread = null

  private[muscle] def contractionLevel : Int = {currentContractionLevel.get()}
  private[muscle] def isContracting : Boolean = {contracting.get()}

  /**
   * Contracts the muscle.  If the muscle is currently contracting then this method will block until the current contraction
   * cycle has finished before starting the new contraction request
   * @param requiredContractionLevel The level of contraction we want the muscle to move to (100% = full contracted)
   * @param speed The speed we want it to do it at which will be the number of contractions per second
   *              (1 contaction = 1% of total contraction so, for example, 5 would mean contract at 5% per second)
   * @return false if the parameters were invalid or the movement could not occur e.g. if the muscle was contracted to a
   *         level of 40 and a contraction level of 20 was provided or if a contraction level of >100 was provided
   */
  private[muscle] def startContracting(requiredContractionLevel : IntegerPercent, speed : Int): Boolean = {

    contractingMutex.synchronized {

      if (requiredContractionLevel == IntegerPercentage(0) || requiredContractionLevel <= currentContractionLevel.get()) {
        return false
      }

      stopContracting()

      //Start off a new contraction
      contractorThread = new Thread(new ContractorRunnable(requiredContractionLevel, speed))
      contractorThread.start()
      return true
    }
  }

  private[muscle] def detract() : Unit = {
    contractingMutex.synchronized {
      currentContractionLevel.set(currentContractionLevel.get() - 1)
    }
  }

  /*
   * Stop the current contraction if there is one.  This method will block until the current contraction cycle has finished.
   */
  private[muscle] def stopContracting() {
    if(contractorThread != null && contractorThread.isAlive) {
      contracting.set(false);
      contractorThread.join()
    }
  }

  val nerveImpluseReceiver : NerveImpulseReceiver = () => Unit: {
    //%%Add a routine that contracts the muscle instantly (1% per impulse) (maybe not on a separate thread if it's in
    //response to a nerve impulse)
  }

  private class ContractorRunnable(requiredContractionLevel : IntegerPercent, contractionsPerSecond : Int) extends Runnable {
    override def run(): Unit = {

      if(startContractingListener != null) {
        startContractingListener()
      }

      contracting.set(true)
      val timeTakenForContraction = round(1000 / contractionsPerSecond)
      var canMove = true
      //Keep contracting at the required speed as long as:
      //1: The contraction level is below the desired contraction level
      //2: There's enough oxygen for our movement
      //3: We haven't been told to stop yet
      while(
        requiredContractionLevel > currentContractionLevel.get() &&
        canMove &&
        contracting.get()) {

        oxygenStoreMutex.synchronized {
          Thread.sleep(timeTakenForContraction)
          if(oxygenStored >= oxygenUsedPerUnitOfContraction) {
            oxygenStored = oxygenStored - oxygenUsedPerUnitOfContraction
            currentContractionLevel.set(currentContractionLevel.get() + 1)
            if(contractedListener != null) {
              contractedListener(Muscle.this,currentContractionLevel.get())
            }
          }
          else {
            //Not enough oxygen left so the muscle can't move any more
            canMove = false
          }
        }
      }
      contracting.set(false)

      if(stopContractingListener != null) {
        stopContractingListener()
      }
    }
  }
}