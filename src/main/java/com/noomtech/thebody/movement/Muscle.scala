package com.noomtech.thebody.movement

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}
import java.util.concurrent.{CountDownLatch, ExecutorService, Executors}
import scala.math._

import com.noomtech.thebody.IntegerPercentage.IntegerPercent
import com.noomtech.thebody.{IntegerPercentage, Oxygen}
import com.noomtech.thebody.buildingblocks.{Named, Pipe}
import com.noomtech.thebody.circulatorysystem.CirculatorySystemOxygenTaker

/**
 * Represents a muscle in the body.  It has a supply of oxygen and can be told to contract to any degree, whereupon it
 * will use its oxygen supply to an extent that is directly proportional to the amount of movement and the muscle's
 * size.
 * @constructor
 * @param pipe The pipe in the circulatory system that this muscle will gets its oxygen supply from
 * @param relativeSize A relative value where 100% is the size of the largest muscle in the body.  This is used to determine
 * properties such as the amount of oxygen the muscle needs to move and its strength
 */
class Muscle(pipe : Pipe, relativeSize : IntegerPercent, name : String) extends Named {

  private val oxygenStored : AtomicInteger = new AtomicInteger(0)
  private val oxygenStoreMutex = new Object()
  private val oxygenUsedPerUnitOfContraction = round(relativeSize * 100).asInstanceOf[Int]

  //Receives the oxygen needed for this muscle from the circulatory system
  def processNewOxygen(oxygen : Oxygen) = {oxygenStoreMutex.synchronized{oxygenStored.set(oxygen.amount)}}
  //CirculatorySystemOxygenTaker will take the oxygen for the muscle
  private def oxygenTaker : CirculatorySystemOxygenTaker =
    new CirculatorySystemOxygenTaker(round(relativeSize * 100).asInstanceOf[Int], processNewOxygen)
  //Add the CirculatorySystemOxygenTaker to the pipe provided
  pipe.addParticleProcessor(oxygenTaker)

  override def getName() : String = {name}

  private val currentContractionLevel : AtomicInteger  = new AtomicInteger(0)
  private val contracting : AtomicBoolean = new AtomicBoolean(false)
  private val contractingMutex = new Object()

  private var contractorThread : Thread = null

  def contractionLevel : Int = {currentContractionLevel.get()}
  def isContracting : Boolean = {contracting.get()}

  /**
   * Contracts the muscle
   * @param requiredContractionLevel The level of contraction we want the muscle to move to (100% = full contracted)
   * @param speed The speed we want it to do it at which will be the number of contractions per second
   *              (1 contaction = 1% of total contraction so, for example, 5 would mean contract at 5% per second)
   * @return false if the parameters were invalid or the movement could not occur e.g. if the muscle was contracted to a
   *         level of 40 and a contraction level of 20 was provided or if a contraction level of >100 was provided
   */
  def startContracting(requiredContractionLevel : IntegerPercent, speed : Int): Boolean = {

    contractingMutex.synchronized {

      if (requiredContractionLevel == IntegerPercentage(0) || requiredContractionLevel <= currentContractionLevel.get()) {
        false
      }

      //Stop the current contraction of there is one and wait for it to finish
      if (contractorThread != null && contractorThread.isAlive) {
        contracting.set(false)
        contractorThread.join()
      }

      //Start off a new contraction
      contractorThread = new Thread(new ContractorRunnable(requiredContractionLevel, speed))
      contractorThread.start()
      true
    }
  }

  private class ContractorRunnable(requiredContractionLevel : IntegerPercent, contractionsPerSecond : Int) extends Runnable {
    override def run(): Unit = {
      contracting.set(true)
      val timeToWait = round(1000 / contractionsPerSecond)
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
          val oxygenAvailable = oxygenStored.get()
          if(oxygenAvailable >= oxygenUsedPerUnitOfContraction) {
            oxygenStored.set((oxygenAvailable - oxygenUsedPerUnitOfContraction))
            currentContractionLevel.set(currentContractionLevel.get() + 1)
            Thread.sleep(timeToWait)
          }
          else {
            //Not enough oxygen left so the muscle can't move any more
            canMove = false
          }
        }
      }
      contracting.set(false)
    }
  }
}