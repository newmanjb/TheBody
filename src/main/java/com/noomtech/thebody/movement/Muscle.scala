package com.noomtech.thebody.movement

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}
import java.util.concurrent.{CountDownLatch, ExecutorService, Executors}

import com.noomtech.thebody.buildingblocks.{OxygenReceiver, Named, Pipe}
import com.noomtech.thebody.circulatorysystem.CirculatorySystemOxygenTaker
import com.noomtech.thebody.utils.Percentage.Percent

/**
 * Represents a muscle in the body.  It has a supply of oxygen and can be told to contract to any degree, whereupon it
 * will use its oxygen supply to an extent that is directly proportional to the amount of movement and the muscle's
 * size.
 * @constructor
 * @param pipe The pipe in the circulatory system that this muscle will gets its oxygen supply from
 * @param size A relative value where 100% is the size of the largest muscle in the body.  This is used to determine
 *             properties such as the amount of oxygen the muscle needs to move and its strength
 */
class Muscle(pipe : Pipe, size : Percent) extends OxygenReceiver with Named {

  //Takes the oxygen needed for this muscle from the circulatory system
  private def oxygenTaker : CirculatorySystemOxygenTaker = new CirculatorySystemOxygenTaker(20,this)
  pipe.addParticleProcessor(oxygenTaker)

  private val oxygenStored : AtomicInteger = new AtomicInteger(0)
  private val oxygenPerContraction = 3
  private val oxygenStoreMutex = new Object()

  override def submitOxygen(oxygen : Int) = {oxygenStored.set(oxygen) }
  override def getName() : String = {"Muscle"}

  val currentContractionLevel : AtomicInteger  = new AtomicInteger(0)
  val contracting : AtomicBoolean = new AtomicBoolean(false)

  private val singleThreadExecutorService : ExecutorService = Executors.newSingleThreadExecutor()
  
  private var contractingCountdownLatch : CountDownLatch = null

  /**
   * Contracts the muscle
   * @param requiredContractionLevel The level of contraction we want the muscle to move to
   * @param speed The speed we want it to do it which will be the number of contractions per second
   * @return false if the parameters were invalid or the movement could not occur e.g. if the muscle was contracted to a
   *         level of 40 and a contraction level of 20 was provided or if a contraction level of >100 was provided
   */
  def startContracting(requiredContractionLevel : Percent, speed : Int): Boolean = {

    if(requiredContractionLevel > 100 || requiredContractionLevel == 0 || requiredContractionLevel <= currentContractionLevel.get()) {
      false
    } 
    
    if (contractingCountdownLatch != null) {
      contracting.set(false)
      contractingCountdownLatch.wait(5000)
     
    }
    
    contractingCountdownLatch = new CountDownLatch(1)
    singleThreadExecutorService.execute(new ContractorRunnable(requiredContractionLevel, contractingCountdownLatch))
    true
  }

  //The
  private class ContractorRunnable(requiredContractionLevel : Percent, countDownLatch: CountDownLatch) extends Runnable {
    override def run(): Unit = {
      contracting.set(true)
      var canMove = true
      while(contracting.get() &&
        canMove &&
        requiredContractionLevel >= currentContractionLevel.get()) {

        oxygenStoreMutex.synchronized {
          val oxygenAvailable = oxygenStored.get()
          if(oxygenAvailable >= oxygenPerContraction) {
            oxygenStored.set((oxygenAvailable - oxygenPerContraction))
            currentContractionLevel.set(currentContractionLevel.get() - 1)
          }
          else {
            canMove = false
          }
        }
      }
      contracting.set(false)
      countDownLatch.countDown()
    }
  }
}